package com.tokopedia.home.test.fragment

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRecommendationFragment

class HomeRecommendationFragmentTest constructor(
        private val mockViewModelFactory: ViewModelProvider.Factory
) : HomeRecommendationFragment(){

    override fun initViewModel() {
        viewModelFactory = mockViewModelFactory
        Log.d("testHomeRecom", viewModelFactory.toString())
        super.initViewModel()
    }

    override fun goToProductDetail(productId: String, position: Int) {

    }
}