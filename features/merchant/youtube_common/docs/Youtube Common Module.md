---
title: "Youtube Common Module"
labels:
- youtube_common
- youtube
---


| **Status** |  <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Willybrodus Rangga Khaisar Purnama](https://tokopedia.atlassian.net/wiki/people/62cb5c393d382dfc9c5f11d2?ref=confluence) [CALVYN JULIAN](https://tokopedia.atlassian.net/wiki/people/5d1adf49cdf26a0d349c76ec?ref=confluence)  |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178)  |
| Release date<br/> | 06 Mar 2020 |
| Module type | <!--start status:BLUE-->INTERNAL-LIBRARY<!--end status-->  |
| Module Location | `com.tokopedia.youtube_common` | `features/merchant/youtube_common` |

## Table of Contents

<!--toc-->

### Background

*In marketing products, there are ways that for some people the advertisements make them interested*  
 *and impressed because they could get an overview of the function and even the form of the product if they used it, the way of marketing was marketing using video media and YouTube is one of the most popular player platforms for the public. Therefore we need to bridge our users by including their video advertisements in the products marketed through our platform. The youtube\_common module was created to help us get video metadata information from YouTube url entered by users.*

### Project Description

*Youtube common module is the internal library to get youtube video metadata detail like description, thumbnails, title, and duration of the video.* The initiative to create a youtube\_common internal library is because we need to accommodate all ways of advertising products by sellers. Besides that, video ads can also help buyers know and even imagine owning the product so that the potential for product purchases increases.

[youtube\_common.webm](/wiki/download/attachments/2127037613/youtube_common.webm?version=1&modificationDate=1673530675508&cacheVersion=1&api=v2)

## How-to

*First, you have to add this dependency to your module:*



```
implementation projectOrAar(rootProject.ext.features.youtubeCommon)
```

First, you need to inject the use case class `GetYoutubeVideoDetailUseCase`



```
@Injectlateinit var getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase
getYoutubeVideoUseCase.setVideoId(youtubeId)
```

then after injecting use case class you need to define the video id or video URL with 



```
getYoutubeVideoUseCase.setVideoId(youtubeId)
```

or



```
getYoutubeVideoUseCase.setVideoUrl(youtubeUrl)
```

after that you need to call `executeOnBackground()` like 



```
getYoutubeVideoUseCase.executeOnBackground()
```

the `executeOnBackground()` will be return `YoutubeVideoDetailModel` object. 

Here is the list of method on `youtube_common` package



| **Parameters** | **Notes** |
| --- | --- |
| `setVideoId(String:youtubeId)` | Method to set video id for identify video tag, snippet, dll |
| `setVideoUrl(String:youtubeurl)` | Method to set video url for identify video tag, snippet, dll |
| `executeOnBackground()` | Method for get video data either using id video or url video  |



---

## FAQ

<!--start expand:Where can I see the features that have been implemented youtube_common ?-->
For example, you can see the youtube\_common implementation on `com.tokopedia.shop` | `feature/merchant/shop_page` also you can see the implementation on `com.tokopedia.product.addedit` | `feature/merchant/product_add_edit`
<!--end expand-->

