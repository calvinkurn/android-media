package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.sellersettings.smartreply.common.util.TalkSmartReplyConstants
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.DaggerTalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.TalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel.TalkSmartReplyDetailViewModel
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.SmartReplyDataWrapper
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.fragment_talk_smart_reply_detail.*
import javax.inject.Inject

class TalkSmartReplyDetailFragment : BaseDaggerFragment(), HasComponent<TalkSmartReplyDetailComponent> {

    @Inject
    lateinit var viewModel: TalkSmartReplyDetailViewModel

    override fun getComponent(): TalkSmartReplyDetailComponent? {
        return activity?.run {
            DaggerTalkSmartReplyDetailComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val cacheManagerId = TalkSmartReplyDetailFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }

            saveInstanceCacheManager?.run {
                val smartReplyDataWrapper = get<SmartReplyDataWrapper>(TalkSmartReplyConstants.SMART_REPLY_DATA_WRAPPER, SmartReplyDataWrapper::class.java)
                viewModel.isSmartReplyOn = smartReplyDataWrapper?.isSmartReplyOn ?: false
                viewModel.messageReady = smartReplyDataWrapper?.messageReady ?: getString(R.string.smart_reply_available_stock_text_area_default_text)
                viewModel.messageNotReady = smartReplyDataWrapper?.messageNotReady ?: getString(R.string.smart_reply_unavailable_stock_text_area_default_text)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_smart_reply_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwitchState()
        initTextArea()
        setDescriptionText()
        setToolbarTitle()
        setupOnBackPressed()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun setDescriptionText() {
        talkSmartReplyDetailSubtitle.text = context?.let { HtmlLinkHelper(it, getString(R.string.smart_reply_settings_description)).spannedString }
    }

    private fun setToolbarTitle() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.setTitle(R.string.title_smart_reply_detail_page)
    }

    private fun initSwitchState() {
        talkSmartReplySwitch.isChecked = viewModel.isSmartReplyOn
        talkSmartReplySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isSmartReplyOn = isChecked
            viewModel.setSmartReply()
            if(isChecked) {
                talkSmartReplyDetailCardContainer.hide()
            } else {
                setCardData()
            }
        }
        if(viewModel.isSmartReplyOn) {
            talkSmartReplyDetailCardContainer.hide()
            return
        }
        setCardData()
    }

    private fun setCardData() {
        talkSmartReplyDetailCardContainer.show()
        talkSmartReplyDetailCard.setData(viewModel.shopName, viewModel.shopAvatar)
    }

    private fun initTextArea() {
        talkSmartReplyDetailAvailableStockTextArea.textAreaInput.setText(viewModel.messageReady)
        talkSmartReplyDetailUnavailableStockTextArea.textAreaInput.setText(viewModel.messageNotReady)
    }

    private fun onSuccessActivateSmartReply() {

    }

    private fun showToaster(text: String) {

    }

    private fun showErrorToaster(text: String) {

    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }
}