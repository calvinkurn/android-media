package com.tokopedia.home_account.linkaccount.view

import android.content.Context
import android.graphics.Typeface
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
import com.tokopedia.webview.WebViewHelper
import javax.inject.Inject

/**
 * Created by Yoris on 04/08/21.
 */

class LinkAccountFragment: BaseDaggerFragment(), AccountItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

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

    //For testing purpose only
    var toggle = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()

        //For testing purpose only
        toggleData()
    }

    //For testing purpose only
    private fun toggleData() {
        adapter.setItems(
            listOf(
                UserAccountDataView(
                    true,
                    userSessionInterface.phoneNumber,
                    "Gojek",
                    "Linked on Aug 4, 2021"
                )
            )
        )
        showFooter()
        binding?.typography?.setOnClickListener {
            if(toggle) {
                adapter.setItems(
                    listOf(
                        UserAccountDataView(
                            true,
                            userSessionInterface.phoneNumber,
                            "Gojek",
                            "Linked on Aug 4, 2021"
                        )
                    )
                )
                showFooter()
            } else {
                adapter.setItems(
                    listOf(
                        UserAccountDataView(
                            false,
                            "Belum terhubung",
                            "Gojek",
                            ""
                        )
                    )
                )
                hideFooter()
            }
            toggle = !toggle
        }
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
        if(data.linkStatus.isNotEmpty()) {
            val linkStatus = data.linkStatus[0]
            adapter.setItems(
                listOf(linkStatus.toUserAccountDataView())
            )
            val isShowFooter = data.linkStatus.any { it.status == "linked" }
            if(isShowFooter){
                showFooter()
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
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            message.indexOf(getString(R.string.label_call_tokopedia)),
            message.indexOf(getString(R.string.label_call_tokopedia)) + getString(R.string.label_call_tokopedia).length,
            0
        )
        binding?.fragmentLinkAccountBottomText?.movementMethod = LinkMovementMethod.getInstance()
        binding?.fragmentLinkAccountBottomText?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun goToTokopediaCareWebview() {
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH
        ))
    }

    private fun initView() {
        binding?.fragmentLinkAccountRv?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.fragmentLinkAccountRv?.adapter = adapter
    }

    override fun onLinkAccountClicked() {
        goToLinkPage()
    }

    override fun onViewAccountClicked() {
        goToLinkPage()
    }

    private fun goToLinkPage() {
        activity?.run {
            Toaster.build(requireView(), getLinkAccountUrl(this, ApplinkConstInternalGlobal.NEW_HOME_ACCOUNT) ?: "", Toaster.LENGTH_LONG).show()
            RouteManager.route(activity, ApplinkConst.WEBVIEW, getLinkAccountUrl(this, ApplinkConstInternalGlobal.NEW_HOME_ACCOUNT))
        }
    }

    private fun LinkStatus.toUserAccountDataView(): UserAccountDataView {
        return UserAccountDataView(
            isLinked = status == "linked",
            status = if(status == "linked") userSessionInterface.phoneNumber else "Belum terhubung",
            partnerName = "Gojek",
            linkDate = "Linked on $linkDate"
        )
    }

    companion object {
        const val BASE_URL = "http://accounts.tokopedia.com/accounts-link/v1/gojek-auth"
        private const val TOKOPEDIA_CARE_PATH = "help"

        fun getLinkAccountUrl(context: Context, redirectionApplink: String): String? {
            var finalUrl = WebViewHelper.appendGAClientIdAsQueryParam(BASE_URL, context)
            finalUrl += "&ld=$redirectionApplink"
            return finalUrl
        }

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LinkAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}