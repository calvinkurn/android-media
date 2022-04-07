package com.tokopedia.foldable

import androidx.appcompat.app.AppCompatActivity
import androidx.window.WindowInfoRepo

class FoldableAndTabletSupportManager(
    windowInfoRepo: WindowInfoRepo,
    foldableInfoCallback: FoldableSupportManager.FoldableInfoCallback,
    activity: AppCompatActivity
) {
    val foldableSupportManager : FoldableSupportManager =
        FoldableSupportManager(windowInfoRepo,foldableInfoCallback,activity)
    val tabletSupportManager : TabletRotationManager = TabletRotationManager(activity)
}