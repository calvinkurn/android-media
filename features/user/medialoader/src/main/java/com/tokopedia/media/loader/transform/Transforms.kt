package com.tokopedia.media.loader.transform

import com.bumptech.glide.load.resource.bitmap.*

abstract class MediaTransformation: BitmapTransformation()

val centerCrop by lazy { CenterCrop() }
val circleCrop by lazy { CircleCrop() }
val fitCenter by lazy { FitCenter() }
val centerInside by lazy { CenterInside() }
fun rotate(degrees: Int) = Rotate(degrees)
fun roundedOf(radius: Int) = RoundedCorners(radius)
