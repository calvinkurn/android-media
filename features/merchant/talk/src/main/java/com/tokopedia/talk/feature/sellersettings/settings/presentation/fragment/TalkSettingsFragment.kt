package com.tokopedia.talk.feature.sellersettings.settings.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.config.GlobalConfig
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
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
        setToolbarTitle()
        showLabel()
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

    private fun setToolbarTitle() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.setTitle(R.string.title_seller_settings_page)
    }

    private fun goToTemplate() {
        if(!GlobalConfig.isSellerApp()) {
            goToSellerMigration()
            return
        }
        val destination = TalkSettingsFragmentDirections.actionTalkSettingsFragmentToTalkTemplateListFragment()
        destination.isSeller = true
        NavigationController.navigate(this@TalkSettingsFragment, destination)
    }

    private fun goToSmartReply() {
        if(!GlobalConfig.isSellerApp()) {
            goToSellerMigration()
            return
        }
        val destination = TalkSettingsFragmentDirections.actionTalkSettingsFragmentToTalkSmartReplySettingsFragment()
        NavigationController.navigate(this@TalkSettingsFragment, destination)
    }

    private fun goToSellerMigration() {
        val intent = context?.let { SellerMigrationActivity.createIntent(it, SellerMigrationFeatureName.FEATURE_DISCUSSION, "", arrayListOf(ApplinkConstInternalGlobal.TALK_SELLER_SETTINGS)) }
        startActivity(intent)
    }


    private fun showLabel() {
        if(!GlobalConfig.isSellerApp()) {
            talkSettingsTemplateLabel.show()
            talkSettingsSmartReplyLabel.show()
            return
        }
        talkSettingsTemplateLabel.hide()
        talkSettingsSmartReplyLabel.hide()
    }

}