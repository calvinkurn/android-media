---
title: "Medialoader: An Enhancement of Image Loader"
labels:
- imageloader
- mediaplatform
- media
- loader
---

<!--left header table-->
| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->                                                                   |
| --- |---------------------------------------------------------------------------------------------------------------------|
| **Project Lead** | [Muh Isfhani Ghiath](https://tokopedia.atlassian.net/wiki/people/5c5b988f0551865e5bc7986c?ref=confluence)           |
| Product Manager | [Gilbert .](https://tokopedia.atlassian.net/wiki/people/612c2c400f8ff40068adbfae?ref=confluence)                    |
| Team | [Minion Jerry]                    |
| Release date | 15 Apr 2021 / <!--start status:GREY-->MA-3.124<!--end status--> <!--start status:GREY-->SA-2.54<!--end status-->    |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->                                                         |
| Product PRD | [Link](https://docs.google.com/document/d/1H__V7uGXLQhlTVRyb5VkHCrcQtM010BLxYP6P-PbIdA/edit#heading=h.9g857dqjo1if) |
| Module Location | `features.medialoader`                                                                                              | `features/media/medialoader` |

## Table of Contents

<!--toc-->

## Overview

### Background

According to [Pingdom](https://www.pingdom.com/blog/page-load-time-really-affect-bounce-rate/), it is said that a page’s load time directly impacts the bounce rate. It indicates that to keep the bounce rate below 10%, we should keep page load time under 3 seconds. Images are a sizeable component on a page that contributes from 6% up to 24% in terms of kilobytes which surely affects times to load the page, *above the fold*.

On the other hand, the network state in Indonesia is not that fast, sometimes users will experience connection below 4G even though they live in big cities. Therefore we decided to deliver smaller image file sizes to those in lower connection, to improve their image-loading experience specifically, and page-load in general.

**Main issues**

1. There is no support for getting adaptive quality images that match the user's internet connection. 

The images displayed on the Tokopedia application will produce the same quality, meanwhile, users with an internet connection with inadequate speed should only get low-quality images to be effective and fast.
2. Has more than one implementation for image loader scattered in various places. The Image Loader feature has many variations in different places, so it will be difficult to maintain and implement the adaptive image itself.

### Project Description

A unified image loader for fetching and showing the images data with adaptive quality based on segmentation of user connection.

1. Adaptive with low-res and high-res Images;
2. Unify library components for all image loaders;
3. Fast and optimize load time.

## How-to

### Default



```
imageView.loadImage(“https://tokopedia.net/sample.png”)
```

### With Custom Properties

Medialoader is highly configurable, so you can set your own properties or override default properties like this:



```
imageView.loadImage(“https://tokopedia.net/sample.png”) {
   ...
   setCacheStrategy(MediaCacheStrategy.RESOURCE)
   setPlaceHolder(R.drawable.custom_place_holder)
   isCircular(true)
   ...
}
```

### Other Extensions



```
imageView.loadImage(“https://tokopedia.net/sample.png”)
imageView.loadImageCircle(“https://tokopedia.net/sample.png”)
imageView.loadImageRounded(“https://tokopedia.net/sample.png”, rounded=10f)
imageView.loadAsGif(“https://tokopedia.net/sample.gif”)
```

Besides URL, you can fetch a Uri or Resource ID also!



```
imageView.loadImage(“file:///android/DCIM/Camera/sample.jpeg”.toUri())
imageView.loadImage(R.drawable.sample_image)
```

If you want to clear the image, you can call this one:



```
imageView.clearImage()
```

### Custom Properties



```
setDelay(timeInMs: Long)
thumbnailUrl(url: String)
useBlurHash(condition: Boolean)
isAnimate(condition: Boolean)
isCircular(condition: Boolean)
setRoundedRadius(radius: Float)
setSignatureKey(key: Key?)
setErrorDrawable(resourceId: Int)
setPlaceHolder(resourceId: Int)
useCache(cache: Boolean)
setCacheStrategy(strategy: MediaCacheStrategy)
overrideSize(newSize: Resize)
decodeFormat(format: MediaDecodeFormat)
transform(transform: Transformation<Bitmap>)
transforms(transforms: List<Transformation<Bitmap>>)
centerCrop()
fitCenter()
centerInside()
listener(onSuccess, onError)
```

### Load image with custom target

You can load images into any view (other than ImageView), let’s take look at how to use it:



```
loadImageWithTarget(context, url, {
  setDelay(3000)
  useBlurHash(true)
}, MediaTarget(relativeLayout, onReady = { _, resource ->
  relativeLayout.setImageBitmap(resource)
}))
```

### Scale type of Medialoader

Medialoader will set your image scale automatically based on your `scaleType` value of ImageView attributes, for example:



```
<ImageView
   ...
   android:scaleType="centerCrop"
   ...
/>
```

## Migration

**ImageHandler**

If you’re still using `ImageHandler` from `tkpdabstraction`, we encourage you to replace:

From:



```
ImageHandler.loadImage2(imageView, “http://tokopedia.net/sample.png”)
```

To:



```
imageView.loadImage(“https://tokopedia.net/sample.png”)
```

**ImageUtils**

Or if you already using `ImageUtils` extensions from `kotlin_extensions`, you can easily replace the import:

From:



```
import com.tokopedia.kotlin.extensions.view.loadImage
```

To:



```
import com.tokopedia.media.loader.loadImage
```

**Blurhash!**

A blurhash has already been implemented on medialoader as a replacement for static placeholder images. To activate the blurhash in your image, please make sure that you’re source image already migrate from `EC7` to our `CDN`.

EC7 → <https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/9/2/80861529/80861529_ee261f0a-0b4a-48ef-be2d-1b87bd3d2cc3_800_800.webp>

CDN → <https://images.tokopedia.net/img/cache/700/emojuk/2020/8/31/89cf3566-2a78-44d2-b00d-9eb7b20ede90.jpg>

and please take note, your hash is already in the URL, like this:

<http://images.tokopedia.net/img/martin/2020/9/7/e0ce27f1-4299-4afe-bb4c-f2fd72c57595.jpg?b=APL4W-D4.AtS>

Which is mean your blurhash is:

`APL4W-D4.AtS`



---

## Action Items

- Create `.loadImage()` extension from **ByteArray** images

## Useful Links

- [Project Deck](https://docs.google.com/presentation/d/1KidkwOvZ7l730Puu-qAK-Ps7_x0xsloqp7pDejFyrOI/edit#slide=id.p)
