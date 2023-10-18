package com.tokopedia.shop.score.stub.penalty.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.shop.score.penalty.presentation.old.activity.ShopPenaltyPageOldActivity
import com.tokopedia.shop.score.stub.penalty.presentation.fragment.ShopPenaltyPageFragmentStub

class ShopPenaltyPageActivityStub: ShopPenaltyPageOldActivity() {

    override fun getNewFragment(): Fragment {
        return ShopPenaltyPageFragmentStub.newInstance()
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopPenaltyPageActivityStub::class.java)
    }
}
