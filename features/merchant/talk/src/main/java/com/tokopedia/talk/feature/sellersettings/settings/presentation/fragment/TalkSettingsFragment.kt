package com.tokopedia.talk.feature.sellersettings.settings.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
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
import com.tokopedia.talk.feature.sellersettings.common.activity.TalkSellerSettingsActivity
import kotlinx.android.synthetic.main.fragment_talk_settings.*

class TalkSettingsFragment : Fragment() {

    companion object {
        const val SMART_REPLY_NAVIGATION = "smartReply"
        const val TEMPLATE_LIST_NAVIGATION = "templateList"
    }

    private var navigation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavigation()
        setToolbarTitle()
        showLabel()
    }

    private fun getDataFromArguments() {
        arguments?.let {
            val talkSettingsFragmentArgs = TalkSettingsFragmentArgs.fromBundle(it)
            navigation = talkSettingsFragmentArgs.navigation
        }
        if(navigation.isGoToSmartReply()) {
            goToSmartReply()
            return
        }
        if(navigation.isGoToTemplateList()) {
            goToTemplate()
            return
        }
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
        if (!GlobalConfig.isSellerApp()) {
            goToSellerMigration(getTemplateListApplink())
            return
        }
        val destination = TalkSettingsFragmentDirections.actionTalkSettingsFragmentToTalkTemplateListFragment()
        destination.isSeller = true
        NavigationController.navigate(this@TalkSettingsFragment, destination)
    }

    private fun goToSmartReply() {
        if (!GlobalConfig.isSellerApp()) {
            goToSellerMigration(getSmartReplyApplink())
            return
        }
        val destination = TalkSettingsFragmentDirections.actionTalkSettingsFragmentToTalkSmartReplySettingsFragment()
        NavigationController.navigate(this@TalkSettingsFragment, destination)
    }

    private fun goToSellerMigration(applink: String) {
        val intent = context?.let { SellerMigrationActivity.createIntent(it, SellerMigrationFeatureName.FEATURE_DISCUSSION, "", arrayListOf(applink)) }
        startActivity(intent)
    }

    private fun showLabel() {
        if (!GlobalConfig.isSellerApp()) {
            talkSettingsTemplateLabel.show()
            talkSettingsSmartReplyLabel.show()
            return
        }
        talkSettingsTemplateLabel.hide()
        talkSettingsSmartReplyLabel.hide()
    }

    private fun getSmartReplyApplink(): String {
        return Uri.parse(ApplinkConstInternalGlobal.TALK_SELLER_SETTINGS).buildUpon().appendQueryParameter(TalkSellerSettingsActivity.KEY_NAVIGATION_PARAM, SMART_REPLY_NAVIGATION).build().toString()
    }

    private fun getTemplateListApplink(): String {
        return Uri.parse(ApplinkConstInternalGlobal.TALK_SELLER_SETTINGS).buildUpon().appendQueryParameter(TalkSellerSettingsActivity.KEY_NAVIGATION_PARAM, TEMPLATE_LIST_NAVIGATION).build().toString()
    }

    private fun String.isGoToSmartReply(): Boolean {
        return this == SMART_REPLY_NAVIGATION
    }

    private fun String.isGoToTemplateList(): Boolean {
        return this == TEMPLATE_LIST_NAVIGATION
    }

}