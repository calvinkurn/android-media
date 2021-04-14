package com.tokopedia.talk.feature.sellersettings.settings.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.config.GlobalConfig
import com.tokopedia.header.HeaderUnify
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.sellersettings.common.activity.TalkSellerSettingsActivity
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController
import com.tokopedia.talk.feature.sellersettings.common.util.UserSessionListener
import com.tokopedia.talk.feature.sellersettings.settings.analytics.TalkSettingsTracking
import com.tokopedia.talk.feature.sellersettings.settings.analytics.TalkSettingsTrackingConstants
import com.tokopedia.talk.feature.sellersettings.settings.presentation.di.DaggerTalkSettingsComponent
import com.tokopedia.talk.feature.sellersettings.settings.presentation.di.TalkSettingsComponent
import com.tokopedia.talk.feature.sellersettings.settings.presentation.widget.TalkSettingsOption

class TalkSettingsFragment : BaseDaggerFragment(), HasComponent<TalkSettingsComponent> {

    companion object {
        const val SMART_REPLY_NAVIGATION = "smartReply"
        const val TEMPLATE_LIST_NAVIGATION = "templateList"
    }

    private var navigation: String = ""
    private var talkSettingsTemplateOption: TalkSettingsOption? = null
    private var talkSettingsSmartReplyOption: TalkSettingsOption? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_settings, container, false)
    }

    override fun getComponent(): TalkSettingsComponent? {
        return activity?.run {
            DaggerTalkSettingsComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun getScreenName(): String {
        return TalkSettingsTrackingConstants.EVENT_CATEGORY_SETTINGS
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0) }?.let { activity?.window?.decorView?.setBackgroundColor(it) }
        bindViewReferences(view)
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
        talkSettingsTemplateOption?.setOnClickListener {
            TalkSettingsTracking.eventClickOptionTemplate(getShopId(), getUserId())
            goToTemplate()
        }
        talkSettingsSmartReplyOption?.setOnClickListener {
            TalkSettingsTracking.eventClickOptionSmartReply(getShopId(), getUserId())
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
            talkSettingsTemplateOption?.showLabel()
            talkSettingsSmartReplyOption?.showLabel()
            return
        }
        talkSettingsTemplateOption?.hideLabel()
        talkSettingsSmartReplyOption?.hideLabel()
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

    private fun bindViewReferences(view: View) {
        talkSettingsTemplateOption = view.findViewById(R.id.talkSettingsTemplateOption)
        talkSettingsSmartReplyOption = view.findViewById(R.id.talkSettingsSmartReplyOption)
    }

    private fun getUserId(): String {
        return (activity as? UserSessionListener)?.getUserId() ?: ""
    }

    private fun getShopId(): String {
        return (activity as? UserSessionListener)?.getShopId() ?: ""
    }

}