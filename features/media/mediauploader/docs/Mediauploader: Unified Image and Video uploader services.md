---
title: "Mediauploader: Unified Image and Video uploader services"
labels:
- uploader
- mediaplatform
- media
- mediauploader
---

<!--left header table-->
| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                               |
| --- |-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Project Lead** | [Muh Isfhani Ghiath](https://tokopedia.atlassian.net/wiki/people/5c5b988f0551865e5bc7986c?ref=confluence) [Calvin Kurniawan](https://tokopedia.atlassian.net/wiki/people/5c6beecd9170244d98d7d0c4?ref=confluence) |
| Product Manager | [Gilbert .](https://tokopedia.atlassian.net/wiki/people/612c2c400f8ff40068adbfae?ref=confluence)                                                                                                                |
| Team | [Minion Jerry]                                                                                                                  |
| Release date | 22 Jan 2020 / <!--start status:GREY-->MA-3.63<!--end status-->                                                                                                                                                  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->                                                                                                                                                     |
| Product PRD | [Link](/wiki/spaces/CO/pages/702507056/PRD+-+Uploadpedia)                                                                                                                                                       |
| Module Location | `features.mediaUploader`                                                                                                                                                                                        | `features/media/mediauploader` |

## Table of Contents

<!--toc-->

## Overview

### Background

A product image is important for the Tokopedia ecosystem as it creates the impression on the buyer. An image enhances the product description which makes it appealing for the visitors of the store. It is not possible for the shopper to feel the product in an eStore, it is the product image they visualize before making the purchase. In this age of visual culture, an image is an essential component that drives your customer’s notion.

### Project Description

Mediauploader is a shared-module that allows to upload data in the form of images or videos to the uploadpedia service. The initiative to create a mediauploader in Q1 2021 is for uploading data in the form of images only, but along with the business development, the Tokopedia ecosystem requires an attraction for sellers and buyers so that they can enjoy the experience of shopping by viewing products on video.

Therefore, mediauploader has developed in Q4 so that it is possible to upload videos with behavior according to the needs of sellers and buyers. Mediauploader makes it possible to upload videos with a very seamless experience. When uploading a new video, we don't have to worry about waiting too long or even losing the video while uploading.

## Flow Diagram

Let's take a look at mediauploader's module architecture overview.
![](https://docs-android.tokopedia.net/images/docs/images_in_zip/image_media_uploader_flow.png)

![](https://docs-android.tokopedia.net/images/docs/mediauploader/Untitled%20Diagram.drawio%20%281%29.png)

And this is the overview of how we set the flow of the video (large) uploaders.

![](https://docs-android.tokopedia.net/images/docs/mediauploader/image-20211207-014024.png)

## How-to

You have to add this deps on your module first:



```
implementation project(rootProject.ext.features.mediauploader)
```

After that, please make sure that you already have the `sourceId` as required for uploading an image/video on mediauploader. If you haven't yet, please ask your BE team or head up to `#help-mediaplatform` to request a new one.

The shared-module used is very simple, you only need to add `MediaUploaderModule` on your dagger component.



```
@Component(
	modules = [MediaUploaderModule::class]
)
interface FooComponent {
	...
}
```

To start the upload an image, you can use the `UploaderUseCase` from mediauploader module. The usecase needs parameters to pass the data you want to upload.



```
@Inject lateinit var uploader: UploaderUseCase

val file = File("/foo/bar/sample.png")

val param = uploader.createParams(
	filePath = file, // required
	sourceId = "" // required
)
```



| Uploader param |
| --- |
| `sourceId` : String | sourceId for define policy request (if you don't have please request to media team) |
| `file` : File | file that will be uploaded |
| `withTranscode` : Boolean | flag to enable transcode feature |
| `isSecure` : Boolean | flag to decide this upload is secure / public (secure will get policy from diff GQL with public even with same `sourceId`) |
| `extraHeader` : Map<String, String> | Map of field & value for extra header field if needed, let empty if you didn’t need to provide extra header |
| `extraBody` : Map<String, String> | Map of field & value for extra body field if needed, let empty if you didn’t need to provide extra body |

This is how we can upload the file look like:



```
launch(dispatchers.io) {
	// we can use the usecase directly like this,
	// because the usecase class use invocation for it.
	val result = uploader(param)

	withContext(dispatchers.main) {
		when (result) {
			is UploadResult.Success -> result.uploadId
			is UploadResult.Error -> result.message
		}
	}
}
```

Uploading a video is similar like how we can upload the image above. But, in the param creators, we have one parameter (optional) to determine **the uploader need to wait for transcoding** or not.



```
@Inject lateinit var uploader: UploaderUseCase

val file = File("/foo/bar/videoplayback.mp4")

val param = uploader.createParams(
	filePath = file, // required
	sourceId = "", // required
	withTranscode = false // optional, default: true
)
```

To show the progress state loader of the uploader, you can do like this:



```
uploader.trackProgress { value ->
	// the value is Int, from 0 to 100%
}
```

*\*) please make sure that you have to execute that* `trackProgress {}` *on the **Main thread**.*

### Upload Result

After successed uploading your image, you’ll get the **uploadId** like this:

`c4cf7a7a-48ae-4ab9-a651-f23424768486`

Or, if you’re uploading a video, you will get the adaptive video url:

`https://vod.tokopedia.com/view/adaptive.m3u8?id=da86367c9cce4b98ae81a567ef8d7fed`



| **type** | **UploadResult.Success** | **UploadResult.Error** |
| --- | --- | --- |
| Image | uploadId | message |
| Video | videoUrl, uploadId | message |



---

## Action Items

- Create a secure uploader for PII <!--start status:BLUE-->ON-PROGRESS<!--end status-->

## FAQ

###### *Any common questions that you couldn’t put in those sections above, you can put them in this FAQ section.*

<!--start expand:Retry video upload?-->
On video large uploader, if the uploader fails in the middle of the progress of the uploader, you can retry it to avoid the uploader starting from the beginning. You can retry it by executing like normal upload, the mediauploader automatically uploads from which part has failed and start the uploader from it.

Maybe some of you are curious about how does mediauploader know which file to resume and which part will be uploaded? We use a local db to store the last state of the uploader. This is the session flow look like:

![](https://docs-android.tokopedia.net/images/docs/mediauploader/Untitled%20Diagram.drawio%20%282%29.png)
<!--end expand-->

<!--start expand:Session Expired-->
Every single video (large) being uploaded has a session at a specific time. For example, if a user tries to upload a file, and then there's no further action in **more than 2 hours**, the session will be expired and the uploader starts will start from the beginning.
<!--end expand-->

<!--start expand:Manual abort a media upload process-->
For now, we only supported video large uploads. you can abort the uploader using `uploader.abortUpload {}` like this.



```
@Inject lateinit var uploader: UploaderUseCase

val file = File("/foo/bar/videoplayback.mp4")

launch {
	abortUpload(
		filePath = file, // required
		sourceId = "" // required
	) {
		withContext(dispatcher.main) {
			// you have to cancel the uploader's coroutine context manually here
		}
	}
}
```
<!--end expand-->

<!--start expand:Debug page for testing-->
We created a debug test page to make the TE team and you test the mediauploader easily on debugging mode. You can access the page by using this applink:

`tokopedia-internal-app://media/uploader`

or, please visit our feature module on `:features:media:mediauploader:debug` to see what the example of implementation looks like. We have a lot of sample implementation in there, such as the mediauploader implementation using `WorkManager`.
<!--end expand-->

