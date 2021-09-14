package com.tokopedia.home_account.linkaccount.view

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
import com.tokopedia.home_account.R
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.databinding.FragmentLinkAccountLayoutBinding
import com.tokopedia.home_account.linkaccount.data.LinkStatus
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.home_account.linkaccount.data.UserAccountDataView
import com.tokopedia.home_account.linkaccount.di.LinkAccountComponent
import com.tokopedia.home_account.linkaccount.listener.AccountItemListener
import com.tokopedia.home_account.linkaccount.view.adapter.LinkAccountAdapter
import com.tokopedia.home_account.linkaccount.viewmodel.LinkAccountViewModel
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

    private lateinit var viewModel: LinkAccountViewModel
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
        ).get(LinkAccountViewModel::class.java)
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
            val isShowFooter = data.response.linkStatus.any { it.status == "linked" }
            if(isShowFooter){
                showFooter()
            } else {
                hideFooter()
            }
        }
    }

    private fun onFailedGetLinkStatus(error: Throwable) {
        if(view != null) {
            Toaster.build(requireView(), error.message ?: "Terjadi kesalahan", Toaster.LENGTH_LONG)
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
                    ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
                }
            },
            message.indexOf("Hubungi"),
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
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.LINK_ACCOUNT_WEBVIEW)
        startActivityForResult(intent, LINK_ACCOUNT_WEBVIEW_REQUEST)
    }

    override fun onViewAccountClicked() {
        homeAccountAnalytics.trackClickViewStatusLinkAccountPage()
        LinkAccountWebViewActivity.gotoSuccessPage(activity, "")
    }

    private fun LinkStatus.toUserAccountDataView(): UserAccountDataView {
        val phone = if(this.phoneNo.isNotEmpty()) {
            this.phoneNo
        } else {
            userSessionInterface.phoneNumber
        }

        return UserAccountDataView(
            isLinked = status == "linked",
            status = if(status == "linked") {
                "+$phone - "
            } else {
                "Belum tersambung"
            },
            partnerName = "Gojek",
            linkDate = "Tersambung ${formatDate(linkedDate)}"
        )
    }

    private fun formatDate(mDate: String): String {
        return try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(mDate) ?: ""
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        } catch (e: Exception) {
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

        const val LINK_ACCOUNT_WEBVIEW_REQUEST = 100

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LinkAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}