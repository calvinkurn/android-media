package com.tokopedia.talk.feature.sellersettings.settings.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController
import com.tokopedia.talk.R
import kotlinx.android.synthetic.main.fragment_talk_settings.*

class TalkSettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavigation()
    }

    private fun setNavigation() {
        talkSettingsTemplateText.setOnClickListener {
            goToTemplate()
        }
        talkSettingsTemplateChevron.setOnClickListener {
            goToTemplate()
        }
        talkSettingsSmartReplyText.setOnClickListener {
            goToSmartReply()
        }
        talkSettingsSmartReplyChevron.setOnClickListener {
            goToSmartReply()
        }
    }

    private fun goToTemplate() {
        val destination = TalkSettingsFragmentDirections.actionTalkSettingsFragmentToTalkTemplateListFragment()
        NavigationController.navigate(this@TalkSettingsFragment, destination)
    }

    private fun goToSmartReply() {
        val destination = TalkSettingsFragmentDirections.actionTalkSettingsFragmentToTalkSmartReplySettingsFragment()
        NavigationController.navigate(this@TalkSettingsFragment, destination)
    }

}