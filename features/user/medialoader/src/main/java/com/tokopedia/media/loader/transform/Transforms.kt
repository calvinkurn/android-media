package com.tokopedia.media.loader.transform

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

typealias RoundedCorners = RoundedCorners
class CenterCrop: CenterCrop()
class CircleCrop: CircleCrop()
class FitCenter: FitCenter()