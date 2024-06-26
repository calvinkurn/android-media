package com.tokopedia.tokopedianow.similarproduct.presentation.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.databinding.ActivityTokopedianowBaseBinding
import com.tokopedia.tokopedianow.similarproduct.presentation.fragment.TokoNowSimilarProductBottomSheetFragment

class TokoNowSimilarProductBottomSheetActivity: BaseActivity() {

    companion object {
        const val EXTRA_SIMILAR_PRODUCT_ID = "extra_similar_product_id"
        const val EXTRA_SIMILAR_PRODUCT_LISTENER = "extra_similar_product_listener"

        fun createNewIntent(context: Context, productId: String, listener: ProductCardCompactSimilarProductTrackerListener?): Intent {
            return Intent(context, TokoNowSimilarProductBottomSheetActivity::class.java).apply {
                putExtra(EXTRA_SIMILAR_PRODUCT_ID, productId)
                putExtra(EXTRA_SIMILAR_PRODUCT_LISTENER, listener)
            }
        }
    }

    private var binding : ActivityTokopedianowBaseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokopedianowBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setOrientation()

        if(savedInstanceState == null) {
            attachFragment()
        }
    }

    private fun getFragment(): Fragment {
        val productId = intent?.getStringExtra(EXTRA_SIMILAR_PRODUCT_ID)
        val listener = intent?.getSerializableExtra(EXTRA_SIMILAR_PRODUCT_LISTENER) as? ProductCardCompactSimilarProductTrackerListener
        return TokoNowSimilarProductBottomSheetFragment.newInstance(productId).apply {
            setListener(listener)
        }
    }

    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun attachFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, getFragment())
            .commit()
    }
}
