package com.tokopedia.chatbot.chatbot2.csat.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.R
import com.tokopedia.csat_rating.dynamiccsat.DynamicCsatConst
import com.tokopedia.csat_rating.dynamiccsat.domain.model.CsatModel

class CsatActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_csat
    }

    @SuppressLint("DeprecatedMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()

        intent.extras?.let {
            val selectedScore: Int = it.getInt(DynamicCsatConst.EXTRA_CSAT_SELECTED_SCORE)
            val csatModel: CsatModel? = it.getParcelable(DynamicCsatConst.EXTRA_CSAT_DATA)
            if (selectedScore > 0 && csatModel != null) {
                val bottomSheet = CsatBottomsheet.newInstance(selectedScore, csatModel)
                bottomSheet.show(supportFragmentManager, "")
            } else {
                finish()
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    companion object {

        fun getInstance(context: Context, selectedScore: Int, csatModel: CsatModel): Intent {
            val intent = Intent(context, CsatActivity::class.java)
            intent.putExtra(DynamicCsatConst.EXTRA_CSAT_SELECTED_SCORE, selectedScore)
            intent.putExtra(DynamicCsatConst.EXTRA_CSAT_DATA, csatModel)
            return intent
        }
    }
}
