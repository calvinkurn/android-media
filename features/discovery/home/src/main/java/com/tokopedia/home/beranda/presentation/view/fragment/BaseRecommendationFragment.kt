package com.tokopedia.home.beranda.presentation.view.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.home.util.HomeRefreshType

abstract class BaseRecommendationFragment : Fragment() {

    abstract fun scrollToTop()
    abstract fun setRefreshType(refreshType: HomeRefreshType)
}
