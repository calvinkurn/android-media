package com.tokopedia.tradein.view.viewcontrollers.activity

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.tradein.R
import com.tokopedia.webview.BaseSessionWebViewFragment
import kotlinx.android.synthetic.main.layout_activity_tradein_info.*

const val tradeInTNCUrl = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-tukar-tambah"
const val blackMarketUrl = ""
const val tradeInTNCSegment = "tradein_tnc"
const val blackMarketSegment = "tradein_black_market"

class TradeInInfoActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        handleIntent()
    }

    private fun initView() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        root_view.minimumHeight = dm.heightPixels.minus(50)
        close_button.setOnClickListener {
            this.finish()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_activity_tradein_info
    }

    private fun handleIntent() {
        if(intent.data?.lastPathSegment == tradeInTNCSegment) {
            val fragment = BaseSessionWebViewFragment.newInstance(tradeInTNCUrl)
            supportFragmentManager.beginTransaction().add(frame_layout.id, fragment).commit()
        } else if(intent.data?.lastPathSegment == blackMarketSegment) {
            val fragment = BaseSessionWebViewFragment.newInstance(blackMarketUrl)
            supportFragmentManager.beginTransaction().add(frame_layout.id, fragment).commit()
        }
    }
}