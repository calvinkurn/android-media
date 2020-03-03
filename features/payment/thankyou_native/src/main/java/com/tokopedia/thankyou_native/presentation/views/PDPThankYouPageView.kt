package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.thankyou_native.R
import javax.inject.Inject

class PDPThankYouPageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    fun getLayout() = R.layout.thank_pdp_recommendation

    init {
        initUI()
    }

    private fun initUI(){
        val v = LayoutInflater.from(context).inflate(getLayout(), this, true)
    }
}