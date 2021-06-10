package com.tokopedia.shop.note.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.note.view.fragment.ShopNoteDetailFragment.Companion.newInstance

/**
 * Created by normansyahputa on 2/8/18.
 */
class ShopNoteDetailActivity : BaseSimpleActivity() {
    private var shopNoteId: String? = null
    private var shopId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(ShopParamConstant.EXTRA_SHOP_ID)
        shopNoteId = intent.getStringExtra(ShopParamConstant.EXTRA_SHOP_NOTE_ID)
        super.onCreate(savedInstanceState)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        toolbar.background = ColorDrawable(
                MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
    }

    override fun getNewFragment(): Fragment? {
        return newInstance(shopId!!, shopNoteId!!)
    }

    companion object {
        fun createIntent(context: Context?, shopId: String?, shopNoteId: String?): Intent {
            val intent = Intent(context, ShopNoteDetailActivity::class.java)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_NOTE_ID, shopNoteId)
            return intent
        }
    }
}