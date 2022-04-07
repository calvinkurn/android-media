package com.tokopedia.foldable

import androidx.appcompat.app.AppCompatActivity

class FoldableAndTabletSupportManager(
    foldableInfoCallback: FoldableSupportManager.FoldableInfoCallback,
    activity: AppCompatActivity
) {
    val tabletSupportManager: TabletRotationManager = TabletRotationManager(activity)
    val foldableSupportManager: FoldableSupportManager =
        FoldableSupportManager(foldableInfoCallback, activity)
}