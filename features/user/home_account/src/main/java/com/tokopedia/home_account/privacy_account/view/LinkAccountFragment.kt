package com.tokopedia.home_account.privacy_account.view

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.home_account.R
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.databinding.FragmentLinkAccountLayoutBinding
import com.tokopedia.home_account.privacy_account.data.LinkStatus
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.privacy_account.data.UserAccountDataView
import com.tokopedia.home_account.privacy_account.di.LinkAccountComponent
import com.tokopedia.home_account.privacy_account.listener.AccountItemListener
import com.tokopedia.home_account.privacy_account.view.adapter.LinkAccountAdapter
import com.tokopedia.home_account.privacy_account.viewmodel.PrivacyAccountViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Yoris on 04/08/21.
 */

class LinkAccountFragment: BaseDaggerFragment(), AccountItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var homeAccountAnalytics: HomeAccountAnalytics

    private lateinit var viewModel: PrivacyAccountViewModel
    private val binding by viewBinding(FragmentLinkAccountLayoutBinding::bind)

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        LinkAccountAdapter(this)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(LinkAccountComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(PrivacyAccountViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_link_account_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        viewModel.getLinkStatus()
    }

    private fun initObserver() {
        viewModel.linkStatus.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> onSuccessGetLinkStatus(it.data)
                is Fail -> onFailedGetLinkStatus(it.throwable)
            }
        })
    }

    private fun onSuccessGetLinkStatus(data: LinkStatusResponse) {
        if(data.response.linkStatus.isNotEmpty()) {
            val linkStatus = data.response.linkStatus.map { it.toUserAccountDataView() }
            adapter.setItems(linkStatus)
            val isShowFooter = data.response.linkStatus.any { it.status == STATUS_LINKED }
            if(isShowFooter){
                showFooter()
            } else {
                hideFooter()
            }
        }
    }

    private fun onFailedGetLinkStatus(error: Throwable) {
        if(view != null) {
            context?.run {
                Toaster.build(requireView(), getString(com.tokopedia.abstraction.R.string.default_request_error_unknown), Toaster.LENGTH_LONG).show()
            }
        }
    }

    private fun hideFooter() {
        binding?.fragmentLinkAccountBottomText?.hide()
    }

    private fun showFooter() {
        binding?.fragmentLinkAccountBottomText?.show()
        val message = getString(R.string.label_call_tokopedia)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToTokopediaCareWebview()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                }
            },
            message.indexOf(CONTACT_INDEX),
            message.length,
            0
        )
        binding?.fragmentLinkAccountBottomText?.movementMethod = LinkMovementMethod.getInstance()
        binding?.fragmentLinkAccountBottomText?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun goToTokopediaCareWebview() {
        homeAccountAnalytics.trackClickHelpPageLinkAcc()
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH
        ))
    }

    private fun initView() {
        binding?.fragmentLinkAccountRv?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.fragmentLinkAccountRv?.adapter = adapter
    }

    override fun onLinkAccountClicked() {
        homeAccountAnalytics.trackClickHubungkanLinkAccountPage()
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW).apply {
            putExtra(
                ApplinkConstInternalGlobal.PARAM_LD,
                LinkAccountWebviewFragment.BACK_BTN_APPLINK
            )
        }
        startActivityForResult(intent, LINK_ACCOUNT_WEBVIEW_REQUEST)
    }

    override fun onViewAccountClicked() {
        homeAccountAnalytics.trackClickViewStatusLinkAccountPage()
        LinkAccountWebViewActivity.gotoSuccessPage(activity, ApplinkConst.HOME)
    }

    private fun LinkStatus.toUserAccountDataView(): UserAccountDataView {
        val phone = if(this.phoneNo.isNotEmpty()) {
            this.phoneNo
        } else {
            userSessionInterface.phoneNumber
        }

        return UserAccountDataView(
            isLinked = status == STATUS_LINKED,
            status = if(status == STATUS_LINKED) {
                String.format(context?.getString(com.tokopedia.home_account.R.string.label_link_acc_phone) ?: "", phone)
            } else {
                context?.getString(com.tokopedia.home_account.R.string.label_link_acc_not_linked) ?: ""
            },
            partnerName = context?.getString(com.tokopedia.home_account.R.string.label_link_acc_gojek) ?: "",
            linkDate = String.format(context?.getString(com.tokopedia.home_account.R.string.label_link_acc_linked_date) ?: "", formatDateLocalTimezone(linkedDate))
        )
    }

    private fun getIndonesiaLocale() : Locale {
        return try {
            Locale("id", "ID")
        }catch (e: Exception) {
            Locale.getDefault()
        }
    }

    fun formatDateLocalTimezone(mDate: String): String {
        return try {
            val date = SimpleDateFormat(SERVER_DATE_FORMAT, getIndonesiaLocale())
            date.timeZone = TimeZone.getTimeZone(TIMEZONE_UTC)
            return SimpleDateFormat(LOCAL_DATE_FORMAT, getIndonesiaLocale()).format(date.parse(mDate) ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            mDate
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == LINK_ACCOUNT_WEBVIEW_REQUEST) {
            viewModel.getLinkStatus(userSessionInterface.phoneNumber.isEmpty())
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        homeAccountAnalytics.trackClickBackLinkAccount()
        return super.onFragmentBackPressed()
    }

    companion object {
        private const val TOKOPEDIA_CARE_PATH = "help"

        private const val STATUS_LINKED = "linked"
        private const val CONTACT_INDEX = "Hubungi"

        private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        private const val LOCAL_DATE_FORMAT = "dd MMM yyyy"
        private const val TIMEZONE_UTC = "UTC"

        const val LINK_ACCOUNT_WEBVIEW_REQUEST = 100

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LinkAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}