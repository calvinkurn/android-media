package com.tokopedia.videorecorder.utils

import com.tokopedia.cameraview.SizeSelectors
import com.tokopedia.cameraview.SizeSelector
import com.tokopedia.cameraview.AspectRatio

/**
 * Created by isfaaghyth on 01/03/19.
 * github: @isfaaghyth
 */
object VideoUtils {

    fun squareSize(): SizeSelector {
        val width = SizeSelectors.minWidth(1000)
        val height = SizeSelectors.minHeight(1000)
        val dimensions = SizeSelectors.and(width, height) // Matches sizes bigger than 1000x1000.
        val ratio = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0f) // Matches 1:1 sizes.
        return SizeSelectors.or(
            SizeSelectors.and(ratio, dimensions), // Try to match both constraints
            ratio, // If none is found, at least try to match the aspect ratio
            ratio // If none is found, take the biggest
        )
    }

    fun ffmegCommand(duration: Int?, sourceFile: String, resultFile: String): Array<String> {
        return arrayOf(
                "-i",
                sourceFile,
                "-vf",
                "trim=0:$duration",
                resultFile
        )
    }

}