package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController
import com.tokopedia.talk.feature.sellersettings.smartreply.common.util.TalkSmartReplyConstants
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.di.DaggerTalkSmartReplySettingsComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.di.TalkSmartReplySettingsComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel.TalkSmartReplySettingsViewModel
import com.tokopedia.talk.R
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.getNavigationResult
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.removeNavigationResult
import com.tokopedia.talk.feature.sellersettings.common.util.TalkSellerSettingsConstants
import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.SmartReplyDataWrapper
import com.tokopedia.talk.feature.sellersettings.template.presentation.fragment.TalkTemplateBottomsheet
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_smart_reply_settings.*
import javax.inject.Inject

class TalkSmartReplySettingsFragment : BaseDaggerFragment(), HasComponent<TalkSmartReplySettingsComponent> {

    @Inject
    lateinit var viewModel: TalkSmartReplySettingsViewModel

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): TalkSmartReplySettingsComponent? {
        return activity?.run {
            DaggerTalkSmartReplySettingsComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSmartReplyData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        showLoading()
        observeSmartReplyData()
        setDescriptionText()
        setToolbarTitle()
        setupOnBackPressed()
        onFragmentResult()
        setSmartReplyInfoClickListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_smart_reply_settings, container, false)
    }

    private fun observeSmartReplyData() {
        viewModel.smartReplyData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    with(it.data) {
                        updateStatisticsData(totalQuestion, totalAnsweredBySmartReply, replySpeed)
                        updateIsSmartReplyOnLabel(isSmartReplyOn)
                        hideLoading()
                    }
                }
                is Fail -> {
                    showErrorToaster()
                }
            }
        })
    }

    private fun updateStatisticsData(totalQuestion: String, totalAnsweredBySmartReply: String, speed: String) {
        talkSmartReplyStatisticsWidget.setData(totalQuestion, totalAnsweredBySmartReply, speed)
    }

    private fun updateIsSmartReplyOnLabel(isActive: Boolean) {
        talkStockSubtitleChevron.setOnClickListener {
            val cacheManagerId = putDataIntoCacheManager()
            if(cacheManagerId.isNullOrEmpty()) {
                return@setOnClickListener
            }
            val destination = TalkSmartReplySettingsFragmentDirections.actionTalkSmartReplySettingsFragmentToTalkSmartReplyDetailFragment()
            destination.cacheManagerId = cacheManagerId
            NavigationController.navigate(this, destination)
        }
        if(isActive) {
            talkIsSmartReplyOnLabel.apply {
                setLabel(getString(R.string.smart_reply_active_label))
                setLabelType(Label.GENERAL_LIGHT_GREEN)
            }
            return
        }
        talkIsSmartReplyOnLabel.apply {
            setLabel(getString(R.string.smart_reply_inactive_label))
            setLabelType(Label.GENERAL_LIGHT_GREY)
        }
    }

    private fun setDescriptionText() {
        talkSmartReplySettingsDescription.text = context?.let { HtmlLinkHelper(it, getString(R.string.smart_reply_settings_description)).spannedString }
    }

    private fun setToolbarTitle() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.setTitle(R.string.title_smart_reply_settings_page)
    }

    private fun putDataIntoCacheManager(): String {
        (viewModel.smartReplyData.value as? Success)?.data?.let {
            val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
            cacheManager?.apply {
                put(TalkSmartReplyConstants.SMART_REPLY_DATA_WRAPPER, SmartReplyDataWrapper(it.isSmartReplyOn, it.messageReady, it.messageNotReady))
            }
            return cacheManager?.id ?: ""
        }
        return ""
    }

    private fun showErrorToaster() {
        view?.let {
            Toaster.build(it, getString(R.string.inbox_toaster_connection_error), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_retry), View.OnClickListener { viewModel.getSmartReplyData() }). show() }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun onFragmentResult() {
        getNavigationResult(TalkTemplateBottomsheet.REQUEST_KEY)?.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.getString(TalkSellerSettingsConstants.KEY_ACTION, "") == TalkSellerSettingsConstants.VALUE_ADD_EDIT) {
                    getSmartReplyData()
                    showLoading()
                }
            }
            removeNavigationResult(TalkTemplateBottomsheet.REQUEST_KEY)
        })
    }

    private fun getSmartReplyData() {
        viewModel.getSmartReplyData()
    }

    private fun showLoading() {
        talkSmartReplySettingsLoading.show()
    }

    private fun hideLoading() {
        talkSmartReplySettingsLoading.hide()
    }

    private fun setSmartReplyInfoClickListener() {
        talkSmartReplySubtitle.setOnClickListener {
            RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.SMART_REPLY_INFORMATION_PAGE}")
        }
    }

}