package com.tokopedia.media.loader.transform

import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

abstract class MediaTransformation: BitmapTransformation()

typealias RoundedCorners = RoundedCorners
class CenterCrop: CenterCrop()
class CircleCrop: CircleCrop()
class FitCenter: FitCenter()
class CenterInside: CenterInside()
class Rotate(degreesToRotate: Int): Rotate(degreesToRotate)