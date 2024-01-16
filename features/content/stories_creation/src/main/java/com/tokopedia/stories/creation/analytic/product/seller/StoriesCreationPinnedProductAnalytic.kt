package com.tokopedia.stories.creation.analytic.product.seller

import com.tokopedia.content.product.picker.seller.analytic.ContentPinnedProductAnalytic
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class StoriesCreationPinnedProductAnalytic @Inject constructor(
    
) : ContentPinnedProductAnalytic {

    override fun onClickPinProductLiveRoom(productId: String) {
        
    }

    override fun onClickPinProductBottomSheet(productId: String) {
        
    }

    override fun onImpressPinProductLiveRoom(productId: String) {
        
    }

    override fun onImpressPinProductBottomSheet(productId: String) {
        
    }

    override fun onImpressFailPinProductLiveRoom() {
        
    }

    override fun onImpressFailPinProductBottomSheet() {
        
    }

    override fun onImpressFailUnPinProductLiveRoom() {
        
    }

    override fun onImpressFailUnPinProductBottomSheet() {
        
    }

    override fun onImpressColdDownPinProductSecondEvent(isLiveRoom: Boolean) {
        
    }
}
