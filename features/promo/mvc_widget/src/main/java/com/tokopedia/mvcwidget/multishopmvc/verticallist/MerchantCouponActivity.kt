package com.tokopedia.mvcwidget.multishopmvc.verticallist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.di.components.DaggerMvcComponent
import com.tokopedia.mvcwidget.di.components.MvcComponent

class MerchantCouponActivity : BaseSimpleActivity() , HasComponent<MvcComponent> {

    override fun getNewFragment(): Fragment {
        return MerchantCouponFragment.newInstance(intent.extras)
    }

    override fun getComponent(): MvcComponent {
       return DaggerMvcComponent.builder()
           .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
           .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
        updateTitle(getString(R.string.mvc_kupon_toko))
    }

    companion object{
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, MerchantCouponActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getMerchantCoupon(context: Context, extras: Bundle): Intent {
            return getCallingIntent(context, extras)
        }
    }
}
