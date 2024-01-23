package com.tokopedia.buy_more_get_more.minicart.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.tokopedia.applink.bmsm.BmsmMiniCartDeepLinkMapper
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.presentation.bottomsheet.GwpMiniCartEditorBottomSheet

/**
 * Created by @ilhamsuaib on 02/12/23.
 */

class GwpMiniCartEditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
        setTransparentStatusBar()
        showBottomSheet(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        showBottomSheet(intent)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("DeprecatedMethod")
    //https://developer.android.com/reference/android/content/Intent#getParcelableExtra(java.lang.String,%20java.lang.Class%3CT%3E)
    private fun showBottomSheet(intent: Intent?) {
        if (supportFragmentManager.isStateSaved) return
        val param = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BmsmMiniCartDeepLinkMapper.EXTRA_PARAM,
                MiniCartParam::class.java
            )
        } else {
            intent?.getParcelableExtra(BmsmMiniCartDeepLinkMapper.EXTRA_PARAM)
        }
        val offerEndData = intent?.getStringExtra(BmsmMiniCartDeepLinkMapper.OFFER_END_DATE)
        val bottomSheet = GwpMiniCartEditorBottomSheet.newInstance().apply {
            setOnDismissListener {
                this@GwpMiniCartEditorActivity.finish()
            }
            setParameter(param ?: MiniCartParam(), offerEndData.orEmpty())
        }

        bottomSheet.show(supportFragmentManager)
    }

    private fun setTransparentStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}