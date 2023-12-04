package com.tokopedia.universal_sharing.view.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateUtil
import com.tokopedia.applink.RouteManager
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.databinding.ActivityDeeplinkIsNotFoundBinding
import com.tokopedia.utils.view.binding.viewBinding

class DeeplinkNotFoundActivity : BaseActivity() {

    private var binding: ActivityDeeplinkIsNotFoundBinding? by viewBinding()

    private var type = ""
    private var source = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.extras?.let { ex ->
            if (ex.containsKey(KEY_TYPE)) {
                type = intent.getStringExtra(KEY_TYPE) ?: ""
            }
            if (ex.containsKey(KEY_SOURCE)) {
                source = intent.getStringExtra(KEY_SOURCE) ?: ""
            }
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_deeplink_is_not_found)
        initView()
    }

    private fun initView() {
        val globalError = binding?.globalError
        val isUpdateType = type == TYPE_UPDATE || isAppUnderMinVersion()
        globalError?.let { ge ->
            if (isUpdateType) {
                ge.errorTitle.text = getString(R.string.title_update_app)
                ge.errorDescription.text =
                    getString(R.string.description_error_not_found)
                ge.errorAction.text = getString(R.string.cta_update_app)
                ge.setActionClickListener {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(APPLINK_PLAYSTORE + packageName)
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(URL_PLAYSTORE + packageName)
                                )
                            )
                        } catch (anfe: ActivityNotFoundException) {
                            RouteManager.route(this, URL_PLAYSTORE + packageName)
                        }
                    }
                    if (source == TYPE_SHARE) {
                        TrackUtil.sendClickUpdateAppEvent(intent?.data)
                    }
                }
            } else {
                ge.errorTitle.text = getString(R.string.title_deeplink_not_found)
                ge.errorDescription.text =
                    getString(R.string.description_deeplink_not_found)
                ge.errorAction.text = getString(R.string.cta_deeplink_not_found)
                ge.setActionClickListener {
                    finish()
                }
            }
        }
        if (source == TYPE_SHARE) {
            TrackUtil.sendImpressionPageEvent(intent?.data)
        }
    }

    private fun isAppUnderMinVersion(): Boolean {
        return AppUpdateUtil.needUpdate(this)
    }

    companion object {
        const val APPLINK_PLAYSTORE = "market://details?id="
        const val URL_PLAYSTORE = "https://play.google.com/store/apps/details?id="
        const val KEY_TYPE = "type"
        const val KEY_SOURCE = "source"
        const val TYPE_UPDATE = "update"
        const val TYPE_SHARE = "share"
    }
}
