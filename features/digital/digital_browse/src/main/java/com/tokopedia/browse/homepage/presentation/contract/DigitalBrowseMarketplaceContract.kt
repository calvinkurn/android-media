package com.tokopedia.browse.homepage.presentation.contract

import android.content.Context

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel

/**
 * @author by furqan on 30/08/18.
 */

interface DigitalBrowseMarketplaceContract {

    interface View : CustomerView {

        fun getCategoryItemCount(): Int

        val fragmentContext: Context?

        fun renderData(marketplaceData: DigitalBrowseMarketplaceViewModel)

        fun showGetDataError(e: Throwable)

    }

    interface Presenter {

        fun onInit()

        fun getMarketplaceDataCloud()

        fun onDestroyView()
    }

}
