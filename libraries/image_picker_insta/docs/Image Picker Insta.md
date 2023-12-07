---
title: "Image Picker Insta"
labels:
- feed
- content
- ccp
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 15 Oct 2021 / <!--start status:BLUE-->MA-3.147<!--end status-->  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->  |
| Product PRD | - |
| Module Location | `libraries.image_picker_insta` | `libraries/image_picker_insta` |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:15 Oct 21 - MA-3.147 / SA-2.077-->
Released  
<https://github.com/tokopedia/android-tokopedia-core/pull/22050>
<!--end expand-->

## Overview

**Image Picker Insta** is a library module used to selecting single / multiple media (images or videos). Even though it is a library module, it is more appropriate to be called as sub-features instead of library (I don’t know the reason why the creator put this as a library). The functionality is pretty much the same with `mediapicker` module from Media Team, but as for now this Image Picker Insta is used only for **Feed Create Post** journey.

### Background

The first journey of uploading a feed post is selecting media. Users are allowed to select a single / multiple images, videos, or both. Actually we don’t know the reason why we are not reusing `mediapicker` from Media team, but I believe the reason is because feed team wants a media picker with different UI than the media team has provided.

### Project Description

Image Picker Insta has 2 pages:

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-17%20at%2016.29.44-20230117-092949.png" alt="" />

In `Media Picker Page`, user is able to:

1. Select a single or multiple media (images, videos, or both).
2. Filter media by Album.
3. Cropping & positioning selected images.
4. Change account if user has multiple account.

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-17%20at%2016.29.57-20230117-093003.png" alt="" />

In `Camera Page`, user is able to:

1. Take a picture or recording a video
2. Switch camera (front & back)

## Tech Stack

- **ViewModel** : some of the logic are stored inside viewmodel, even thought there are still some business logic that are placed in view. [Click here to see the documentation](https://developer.android.com/topic/libraries/architecture/viewmodel).
- **Flow :** is used to retrieve the media from storage. [Click here to see the documentation](https://developer.android.com/kotlin/flow).
- **MediaStore** : is used to query the medias from device external storage. [Click here to see documentation](https://developer.android.com/training/data-storage/shared/media).
- **CameraView by natario1** : is used to capture a picture / recording a video. [Click here to see the documentation](https://github.com/natario1/CameraView).
- **Dynamic Feature** : not all users will use this feature. So to save some APK size, we setup this features as DF On-Demand on MainApp. [Click here to see the official documentation](https://developer.android.com/guide/playcore/feature-delivery)

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/Image%20Picker%20Insta.drawio-20230127-062243.png" alt="" />

## Navigation

[MA - Image Picker Insta - Feed.mov](/wiki/download/attachments/2139065547/MA%20-%20Image%20Picker%20Insta%20-%20Feed.mov?version=1&modificationDate=1674547798030&cacheVersion=1&api=v2)

[MA - Image Picker Insta - User Profile.mov](/wiki/download/attachments/2139065547/MA%20-%20Image%20Picker%20Insta%20-%20User%20Profile.mov?version=2&modificationDate=1674547816786&cacheVersion=1&api=v2)

[SA - Image Picker Insta - Shop Page.mov](/wiki/download/attachments/2139065547/SA%20-%20Image%20Picker%20Insta%20-%20Shop%20Page.mov?version=1&modificationDate=1674547836489&cacheVersion=1&api=v2)

## How-to

To reuse this library for picking media, you need to add this dependency:



```
dependencies {
  ...
  implementation project(rootProject.ext.features.contentCommon)
  ...  
}
```

To redirect to Image Picker Insta, please refer to this snippet code:



```
val intent = RouteManager.getIntent(context, ApplinkConst.IMAGE_PICKER_V2)

// Provide some necessary data
intent.putExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
intent.putExtra(BundleData.MAX_MULTI_SELECT_ALLOWED, BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED)
intent.putExtra(BundleData.TITLE, getString(feedComponentR.string.feed_post_sebagai))
intent.putExtra(BundleData.APPLINK_FOR_GALLERY_PROCEED, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)

startActivity(intent)

// Need to attach tracker from outside
TrackerProvider.attachTracker(FeedTrackerImagePickerInsta(userSession.shopId))
```

Here is the list of parameters that you can setup:



| **Parameter** | **Description** | **Key** | **Value** |
| --- | --- | --- | --- |
| Title | Setup Image Picker Insta Page Title. | title `BundleData.TITLE` | Any String |
| Max Media Selected | Setup the maximum number of media that can be selected. | max\_multi\_select `BundleData.MAX_MULTI_SELECT_ALLOWED` | Any Int |
| Applink For Gallery Proceed | Setup the target applink destination after user chooses the media. | link\_gall `BundleData.APPLINK_FOR_GALLERY_PROCEED` | Any String (Applink) |
| Applink After Camera Capture | Setup the target applink destination after user capture a picture / record a video. | link\_cam`BundleData.APPLINK_AFTER_CAMERA_CAPTURE` | Any String (Applink) |
| Applink For Back Navigation | Setup the target applink destination when user presses back icon. | link\_back`BundleData.APPLINK_FOR_BACK_NAVIGATION` | Any String (Applink) |
| Is Open From | Setup the source page that open the Image Picker Insta page. | key\_is\_open\_from`BundleData.KEY_IS_OPEN_FROM` | Any String |
| Is Create Post as Buyer | Setup whether we want to limit the account selection to only buyer or not. | is\_create\_post\_as\_buyer`BundleData.IS_CREATE_POST_AS_BUYER` | Boolean`true` -> only buyer account is available`false` -> may have buyer & shop account if any |
| Video Max Seconds | Setup the max video seconds allowed to be chosen. | v\_max\_s`BundleData.VIDEO_MAX_SECONDS` | Any Int |



---

## Action Items

- Add Unit Test
- Move tracker to inside Image Picker Insta

## Useful Links

- [Figma](https://www.figma.com/file/xol2pH7zBm02Cl8dzznVNb/%5BFeed%5D-Post-Creation?node-id=5896%3A166583&t=BjqYYHuDFetU2VVl-0)

## FAQ

<!--start expand:What is the difference from this media picker with the one that owned by Media Team?-->
This media picker is only used for Feed Post Creation. If your feature needs a media picker, please consider to use [media picker](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1944682656/MediaPicker+Universal+Image+and+Video+Picker) from Media Team.

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20220419-021000.png" alt="" />
<!--end expand-->

