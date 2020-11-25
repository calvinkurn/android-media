package com.tokopedia.otp.common.abstraction

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.otp.R

/**
 * Created by Ade Fulki on 21/04/20.
 * ade.hadian@tokopedia.com
 */
abstract class BaseOtpToolbarFragment : BaseOtpFragment() {

    abstract fun getToolbar(): Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(getToolbar())
            it.supportActionBar?.apply {
                setHomeAsUpIndicator(R.drawable.ic_toolbar_back_otp)
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                elevation = 0f
                setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(it, R.color.Unify_N0)))
            }
        }
    }
}