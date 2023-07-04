package com.tokopedia.loginregister.shopcreation.view.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * Created by Ade Fulki on 2019-12-19.
 * ade.hadian@tokopedia.com
 */

abstract class BaseShopCreationFragment : BaseDaggerFragment() {

    abstract fun getToolbar(): Toolbar?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).let {
            getToolbar()?.let { toolbar -> it.setSupportActionBar(toolbar) }

            it.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(
                    it,
                    com.tokopedia.loginregister.R.color.transparent
                )))
            }
        }
    }
}
