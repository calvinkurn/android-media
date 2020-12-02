package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.di.DaggerTalkSmartReplySettingsComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.di.TalkSmartReplySettingsComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel.TalkSmartReplySettingsViewModel
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.Label
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
        viewModel.getSmartReplyData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        observeSmartReplyData()
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
                    }
                }
                is Fail -> {

                }
            }
        })
    }

    private fun updateStatisticsData(totalQuestion: String, totalAnsweredBySmartReply: String, speed: String) {
        talkSmartReplyStatisticsWidget.setData(totalQuestion, totalAnsweredBySmartReply, speed)
    }

    private fun updateIsSmartReplyOnLabel(isActive: Boolean) {
        if(isActive) {
            talkIsSmartReplyOnLabel.apply {
                setLabel(getString(R.string.smart_reply_active_label))
                setLabelType(Label.GENERAL_LIGHT_GREEN)
            }
            talkStockSubtitleChevron.setOnClickListener {
                val destination = TalkSmartReplySettingsFragmentDirections.actionTalkSmartReplySettingsFragmentToTalkSmartReplyDetailFragment()
                NavigationController.navigate(this, destination)
            }
            return
        }
        talkIsSmartReplyOnLabel.apply {
            setLabel(getString(R.string.smart_reply_inactive_label))
            setLabelType(Label.GENERAL_LIGHT_GREY)
        }
    }

    private fun showError() {

    }

    private fun hideError() {

    }

}