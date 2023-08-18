package com.tokopedia.editor.ui.placement

import androidx.lifecycle.ViewModel
import com.tokopedia.editor.ui.model.ImagePlacementModel
import javax.inject.Inject

class PlacementImageViewModel @Inject constructor(): ViewModel() {
    var imagePath = ""
    var placementModel: ImagePlacementModel? = null
}
