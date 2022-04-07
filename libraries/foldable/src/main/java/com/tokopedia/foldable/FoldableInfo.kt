package com.tokopedia.foldable

import android.graphics.Rect
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.window.DisplayFeature
import androidx.window.FoldingFeature
import androidx.window.WindowLayoutInfo

class FoldableInfo(newLayoutInfo: WindowLayoutInfo) {
    var windowLayoutInfo: WindowLayoutInfo = newLayoutInfo
        private set
    var foldingFeature: FoldingFeature? = null
        private set

    init {
        foldingFeature = getFoldingFeature(newLayoutInfo)
    }


    internal fun setWindowLayoutInfo(newLayoutInfo: WindowLayoutInfo) {
        windowLayoutInfo = newLayoutInfo
        foldingFeature = getFoldingFeature(newLayoutInfo)
    }


    fun isTableTopMode(): Boolean {
        return foldingFeature?.let {
            it.state == FoldingFeature.STATE_HALF_OPENED &&
                    it.orientation == FoldingFeature.ORIENTATION_HORIZONTAL
        } ?: false
    }

    fun isBookMode(): Boolean {
        return foldingFeature?.let {
            it.state == FoldingFeature.STATE_HALF_OPENED &&
                    it.orientation == FoldingFeature.ORIENTATION_VERTICAL
        } ?: false
    }

    fun isFoldableDevice(): Boolean {
        return foldingFeature != null
    }

    fun isHalfOpen(): Boolean {
        return foldingFeature?.let { it ->
            it.state == FoldingFeature.STATE_HALF_OPENED
        } ?: false
    }

    private fun getFoldingFeature(newLayoutInfo: WindowLayoutInfo): FoldingFeature? {
        return newLayoutInfo.displayFeatures.filterIsInstance(FoldingFeature::class.java)
            .firstOrNull()
    }

    /**
     * Get the bounds of the display feature translated to the View's coordinate space and current
     * position in the window. This will also include view padding in the calculations.
     */
    fun getDisplayFeatureBounds(
        rootView: View,
        includePadding: Boolean = true
    ): Rect? {
        return foldingFeature?.let {
            getFeatureBoundsInWindow(it, rootView, includePadding)
        }
    }

    /**
     * Get the bounds of the display feature translated to the View's coordinate space and current
     * position in the window. This will also include view padding in the calculations.
     */
    private fun getFeatureBoundsInWindow(
        displayFeature: DisplayFeature,
        view: View,
        includePadding: Boolean = true
    ): Rect? {
        // The the location of the view in window to be in the same coordinate space as the feature.
        val viewLocationInWindow = IntArray(2)
        view.getLocationInWindow(viewLocationInWindow)

        // Intersect the feature rectangle in window with view rectangle to clip the bounds.
        val viewRect = Rect(
            viewLocationInWindow[0], viewLocationInWindow[1],
            viewLocationInWindow[0] + view.width, viewLocationInWindow[1] + view.height
        )

        // Include padding if needed
        if (includePadding) {
            viewRect.left += view.paddingLeft
            viewRect.top += view.paddingTop
            viewRect.right -= view.paddingRight
            viewRect.bottom -= view.paddingBottom
        }

        val featureRectInView = Rect(displayFeature.bounds)
        val intersects = featureRectInView.intersect(viewRect)

        // Checks to see if the display feature overlaps with our view at all
        if ((featureRectInView.width() == 0 && featureRectInView.height() == 0) ||
            !intersects
        ) {
            return null
        }

        // Offset the feature coordinates to view coordinate space start point
        featureRectInView.offset(-viewLocationInWindow[0], -viewLocationInWindow[1])

        return featureRectInView
    }


    fun alignSeparatorViewToFoldingFeatureBounds(
        constraintSet: ConstraintSet,
        rootView: View,
        separatingViewID: Int
    ): ConstraintSet {

        // Get and translate the feature bounds to the View's coordinate space and current
        // position in the window.
        if (!isFoldableDevice())
            return constraintSet
        val bounds = foldingFeature?.let {
            getFeatureBoundsInWindow(it, rootView)
        }
        bounds?.let { rect ->
            val horizontalFoldingFeatureHeight = (rect.bottom - rect.top).coerceAtLeast(0)
            val verticalFoldingFeatureWidth = (rect.right - rect.left).coerceAtLeast(0)

            // Sets the view to match the height and width of the folding feature
            constraintSet.constrainHeight(
                separatingViewID,
                horizontalFoldingFeatureHeight
            )
            constraintSet.constrainWidth(
                separatingViewID,
                verticalFoldingFeatureWidth
            )

            constraintSet.connect(
                separatingViewID, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START, 0
            )
            constraintSet.connect(
                separatingViewID, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0
            )

            if (foldingFeature?.orientation == FoldingFeature.ORIENTATION_VERTICAL) {
                constraintSet.setMargin(separatingViewID, ConstraintSet.START, rect.left)
            } else {
                // FoldingFeature is Horizontal
                constraintSet.setMargin(
                    separatingViewID, ConstraintSet.TOP,
                    rect.top
                )
            }
            // Set the view to visible and apply constraints
            constraintSet.setVisibility(separatingViewID, View.VISIBLE)
        }
        return constraintSet
    }
}