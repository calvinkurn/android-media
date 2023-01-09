package com.tokopedia.samples.foldables

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.window.FoldingFeature
import com.tokopedia.foldable.FoldableInfo
import com.tokopedia.foldable.FoldableSupportManager
import com.tokopedia.samples.R

class FoldableActivity : AppCompatActivity(), FoldableSupportManager.FoldableInfoCallback {
    lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.foldable_activity_layout)
        constraintLayout = findViewById(R.id.parent_container)
        FoldableSupportManager(this,this)
    }

    override fun onChangeLayout(foldableInfo: FoldableInfo) {
        if (foldableInfo.isFoldableDevice() && foldableInfo.foldingFeature?.orientation == FoldingFeature.ORIENTATION_VERTICAL) {
            val set = ConstraintSet().apply { clone(constraintLayout) }
            val newSet = foldableInfo.alignSeparatorViewToFoldingFeatureBounds(
                set,
                findViewById<View>(android.R.id.content).rootView,
                R.id.separator
            )
            newSet.connect(R.id.container_1, ConstraintSet.END, R.id.separator, ConstraintSet.START)
            newSet.connect(R.id.container_2, ConstraintSet.START, R.id.separator, ConstraintSet.END)
            newSet.applyTo(constraintLayout)
        } else {
            val set = ConstraintSet().apply { clone(constraintLayout) }
            set.connect(
                R.id.container_1,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )
            set.connect(
                R.id.container_2,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )
            set.applyTo(constraintLayout)
        }
    }
}
