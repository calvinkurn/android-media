package com.tokopedia.cod

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.transactiondata.entity.response.cod.Data

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodActivity : BaseSimpleActivity() {

    companion object {

        const val EXTRA_COD_DATA = "EXTRA_COD_DATA"

        @JvmStatic
        fun newIntent(context: Context, data: Data): Intent {
            val intent = Intent(context, CodActivity::class.java)
            intent.putExtra(EXTRA_COD_DATA, data)
            return intent
        }
    }

    override fun getScreenName(): String {
        return getString(R.string.cod_brand_tag)
    }

    override fun getNewFragment(): Fragment {
        val data:Data = intent.getParcelableExtra(EXTRA_COD_DATA)
        return CodFragment.newInstance(data)
    }

}