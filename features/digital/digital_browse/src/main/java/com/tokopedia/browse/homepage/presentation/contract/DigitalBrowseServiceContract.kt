package com.tokopedia.browse.homepage.presentation.contract

import android.content.Context

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel
import com.tokopedia.browse.homepage.presentation.model.IndexPositionModel

/**
 * @author by furqan on 30/08/18.
 */

interface DigitalBrowseServiceContract {

    interface View : CustomerView {

        val fragmentContext: Context?

        fun getItemCount(): Int

        fun renderData(viewModel: DigitalBrowseServiceViewModel)

        fun showTab()

        fun hideTab()

        fun addTab(key: String)

        fun renderTab(selectedTabIndex: Int)

        fun showGetDataError(e: Throwable)
    }

    interface Presenter {

        fun onInit()

        fun getDigitalCategoryCloud()

        fun processTabData(titleMap: Map<String, IndexPositionModel>, viewModel: DigitalBrowseServiceViewModel, categoryId: Int)

        fun getItemPositionInGroup(titleMap: Map<String, IndexPositionModel>, itemPositionInList: Int): DigitalBrowseServiceAnalyticsModel

        fun onDestroyView()

    }
}
