package com.tokopedia.search.result.product.dialog

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.orNone
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class BottomSheetInappropriateViewDelegate @Inject constructor(
    @SearchContext
    context: Context,
    fragmentProvider: FragmentProvider,
    queryKeyProvider: QueryKeyProvider,
    searchParameterProvider: SearchParameterProvider,
    private val userSession: UserSessionInterface,
    private val viewUpdater: ViewUpdater,
) : BottomSheetInappropriateView,
    ContextProvider by WeakReferenceContextProvider(context),
    SearchParameterProvider by searchParameterProvider,
    QueryKeyProvider by queryKeyProvider,
    FragmentProvider by fragmentProvider{

    companion object {
        private const val TEMPORARY_UNSAFE_SEARCH = "03.65.00.00 "
    }

    override fun openInappropriateWarningBottomSheet(
        item: ProductItemDataView,
        isAdult: Boolean,
        onButtonConfirmationClicked: () -> Unit,
    ) {
        val bottomSheet = BottomSheetInappropriate.newInstance(isAdult)
        bottomSheet.setOnButtonConfirmationClicked {
            trackOnClickConfirmation(item)
            onButtonConfirmationClicked.invoke()
            showMessageSuccessInactiveSafeProduct()
            viewUpdater.unBlurItem()
        }
        bottomSheet.show(
            getFragment().parentFragmentManager,
            bottomSheet.tag
        )
    }

    private fun showMessageSuccessInactiveSafeProduct() {
        val view = getFragment().view ?: return
        val message = view.context.getString(R.string.search_result_product_inappropriate_success_inactive)
        view.let {
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun trackOnClickConfirmation(
        item: ProductItemDataView,
    ) {
        val searchParameter = getSearchParameter()?.getSearchParameterMap() ?: mapOf()
        val dimension90 = Dimension90Utils.getDimension90(searchParameter)

        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                SearchComponentTrackingConst.BUSINESSUNIT, SearchComponentTrackingConst.SEARCH,
                SearchComponentTrackingConst.CAMPAIGNCODE, SearchTrackingConstant.NONE,
                SearchTrackingConstant.EVENT, SearchComponentTrackingConst.Event.CLICKSEARCH,
                SearchTrackingConstant.EVENT_CATEGORY, SearchComponentTrackingConst.Category.SEARCH_COMPONENT,
                SearchTrackingConstant.EVENT_ACTION, SearchComponentTrackingConst.Action.CLICK_OTHER_ACTION,
                SearchTrackingConstant.EVENT_LABEL, "keyword:$queryKey | value_name:temporary-unsafe-search",
                SearchComponentTrackingConst.COMPONENT, TEMPORARY_UNSAFE_SEARCH,
                SearchComponentTrackingConst.PAGESOURCE, dimension90.orNone(),
                SearchComponentTrackingConst.CURRENTSITE, SearchComponentTrackingConst.TOKOPEDIAMARKETPLACE,
                SearchComponentTrackingConst.PAGEDESTINATION, item.applink.orNone(),
                SearchTrackingConstant.USER_ID, userSession.userId,
                SearchEventTracking.EXTERNAL_REFERENCE, SearchTrackingConstant.NONE,
            )
        )
    }
}
