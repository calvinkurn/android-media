---
title: "Play Shorts"
labels:
- ccp
- play-shorts
- play
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| **Contributors** | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)  |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence) [Fadlim . (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/61e501ac98cd6100703021eb?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 10 Feb 2023 <!--start status:BLUE-->MA-3.207<!--end status--> <!--start status:GREEN-->SA-2.137<!--end status--> |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [PRD](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1942370607/PRD+-+Short+Video+Content+Creation+Apps) |
| Module Location | `features.playBroadcaster` | `features/content/play_broadcaster` (Play Shorts is on the same module as Play Broadcaster). |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:24 Feb 23 (MA-3.210/SA-2.140)-->
1. Fix Success Upload Notification Redirection When App is on Background  
<https://github.com/tokopedia/android-tokopedia-core/pull/31853>
<!--end expand-->

<!--start expand:17 Feb 23 (MA-3.208/SA-2.138)-->
1. Banned & Whitelisting Adjustment  
<https://github.com/tokopedia/android-tokopedia-core/pull/31444>
2. Fix TnC Text Color  
<https://github.com/tokopedia/android-tokopedia-core/pull/31517>
<!--end expand-->

<!--start expand:10 Feb 23 (MA-3.207/SA-2.137)-->
1. Play Shorts MVP Release  
<https://github.com/tokopedia/android-tokopedia-core/pull/31351>
2. Attach Remote Config on Play Shorts Entry Point (RC)  
<https://github.com/tokopedia/android-tokopedia-core/pull/31476>
3. Fix Text Hint on Title Form (RC)  
<https://github.com/tokopedia/android-tokopedia-core/pull/31491>
<!--end expand-->

## Overview

**Play Shorts** is a feature that enables seller / user to upload short video. This feature can be used by sellers to promote their products and by users to promote other shops product as affiliate. Play Shorts is owned by CMP tribe, especially Content Creation Platform (CCP) team. 

### Background

Short content seems to be loved by many people. As you may know, there are a lots of social media that has dedicated feature for creators to upload a short content. Even in some platform, short content is also used to promote products and the impact is also great. With this data, we try to provide the same experience within Tokopedia ecosystem so users are able to promote their products (for seller) or other products (for UGC).

### Project Description

Play Shorts is located on the same module as Play Broadcaster. The initial reason was Play Shorts reuses a lot of components from Play Broadcaster. But later if it's getting more complex, we will consider to put Play Shorts on a dedicated module.

Here is the list of entry points in both MA & SA:



| **MainApp** | **SellerApp** |
| --- | --- |
| Play Broadcaster | Play Broadcaster |
| Feed | Shop Page |
| User Profile | Search |
|  | Iklan dan Promosi |

When user opens Play Shorts from any entry points, user is asked to select a video. After selecting a video, you will be redirected to Preview Page where you can re-check the selected video or change the video. Both of these pages are features from media picker owned by Media Team.

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20230206-071118.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20230206-071131.png" alt="" />

After user confirms the video, user will be redirected to Preparation Page where user is able to:

1. Input title.
2. Tag Product.
3. **(Optional)** Select Cover.
4. Change Account.

To select a cover, user need to complete the title & tag a product first. If user is not selecting a cover, **the first frame of the video will be taken as the cover.**

<img src="https://docs-android.tokopedia.net/images/docs/res/31d9fa31-52b8-4127-871c-f46ec0ea5c8c%20%281%29-20230206-072002.png" alt="" />

When user clicks `Lanjut` (continue) button, it will redirect user to Summary Page, where user can:

1. See the cover & details about the video.
2. Select a tag that reflecting the content.

**Tag is mandatory, but if the tags are failed to load / no tags, it won’t prevent user from uploading the video.**

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20230206-072404.png" alt="" />

When user clicks `Upload Video` , apps will show a notification that indicates the upload status (in progress, success, error).

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20230206-072536.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20230206-072552.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20230206-072654.png" alt="" />

When the upload is done, user can click the notification to redirect to Play Room and watch the uploaded video.

When the upload is failed, user can click `Coba lagi` (try again) to re-upload the video.

## Tech Stack

- **Dynamic Features (On-Demand & Install-Time)** : not all users will use this feature. So to save some APK size, we setup this features as DF On-Demand on MainApp & DF Install-Time on SellerApp. [Click here to see the official documentation](https://developer.android.com/guide/playcore/feature-delivery)
- **Repository** : a new layer between ViewModel & UseCase. The advantages:


	- Easier to mock network since we only need to mock the repository instead of the usecases.
	- ViewModel constructor won’t be bloated by UseCases anymore.
	- As a place to translate DTO to UI Model.
- **MVI** : we haven’t implemented a full MVI yet, but we are trying to make it MVI. Any user events will be passed to ViewModel with only 1 entry point and the result will be emitted by only 1 UI State, so the pipeline flow is clear and easier to understand.
- **Flow** : previously we are using LiveData, but for new development we start using Flow (StateFlow & SharedFlow). Flow has a lot of useful operator than LiveData, and it’s suitable for our usecase where we combine all individual state to 1 UI state only. [Click here to see the official documentation.](https://developer.android.com/kotlin/flow)
- **Fragment Factory** : Most of the newer features already adopted Fragment Factory. With Fragment Factory, we are able to inject the dependency directly to Fragment constructor. [Click here to see the official documentation.](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory)
- **Adapter Delegate** : We are using Adapter Delegate as replacement to Visitable Pattern. It’s part of our internal library and it’s already used DiffUtil under the hood. [Click here to see the official documentation.](#)
- **View Component** : it’s one of our Play internal library. View Component is used to delegate view logic into 1 dedicated class, so our activity / fragment won’t be bloated by view logics.
- **Work Manager :** used for uploading media to server. [Click here to see the official documentation.](https://developer.android.com/topic/libraries/architecture/workmanager)

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/Play%20Shorts%20-%20Select%20Media.drawio-20230206-082316.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/Play%20Shorts%20-%20Preparation.drawio-20230206-083009.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/Play%20Shorts%20-%20Summary.drawio-20230206-083451.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/Play%20Shorts%20-%20Upload%20Flow.drawio-20230206-084707.png" alt="" />

## Navigation

[Play Shorts Entry Point - MA - Feed.mov](/wiki/download/attachments/2156695768/Play%20Shorts%20Entry%20Point%20-%20MA%20-%20Feed.mov?version=1&modificationDate=1675762766420&cacheVersion=1&api=v2)

[Play Shorts Entry Point - MA - User Profile.mov](/wiki/download/attachments/2156695768/Play%20Shorts%20Entry%20Point%20-%20MA%20-%20User%20Profile.mov?version=1&modificationDate=1675762888180&cacheVersion=1&api=v2)

[Play Shorts Entry Point - MA - Play Broadcaster.mov](/wiki/download/attachments/2156695768/Play%20Shorts%20Entry%20Point%20-%20MA%20-%20Play%20Broadcaster.mov?version=1&modificationDate=1675763076158&cacheVersion=1&api=v2)

[Play Shorts Entry Point - SA - From Shop Page.mov](/wiki/download/attachments/2156695768/Play%20Shorts%20Entry%20Point%20-%20SA%20-%20From%20Shop%20Page.mov?version=3&modificationDate=1675763308081&cacheVersion=1&api=v2)

Note : From SA `Iklan & Promosi` and `Search` is not available yet at the moment. We will provide the screenshot as the entry point is ready.

## How-to

To open Play Broadcaster without any parameter, all you need to do is using `RouteManager.route()` and use `ApplinkConst.PLAY_SHORTS` as the applink.



```
fun openPlayShorts() {
    RouteManager.route(requireContext(), ApplinkConst.PLAY_SHORTS)
}
```

But if you need to send some parameters, you need to add dependency to `content_common` as the helper class for setting up the parameters is in that module.



```
// your module build.gradle
dependencies {
    ...
    implementation projectOrAar(rootProject.ext.features.contentCommon)
    ...
}

// your code
fun openPlayShorts() {
  val appLink = PlayShorts.generateApplink {
      setAuthorType(PlayShortsParam.AuthorType.User)
  }

  RouteManager.route(requireContext(), appLink)
}
```

Here is the list of parameter that you can use within `PlayShorts.generateApplink`:



| **Parameter** | **Description** | **Value** |
| --- | --- | --- |
| setAuthorType | to determine which author is preferred. If you don’t provide this, the last used account will be selected. | `PlayShortsParam.AuthorType.User` || `PlayShortsParam.AuthorType.Shop` |



---

## Action Items

- Move PlayShortsActivity logic to a new parent fragment.

## Useful Links

Please refer to this `Development Docs - Important Links section`

[Play Short Video Content Creation - Development Docs](/wiki/spaces/PA/pages/2077232423/Play+Short+Video+Content+Creation+-+Development+Docs) 

## FAQ

<!--start expand:What testing account that I can use for creating shorts video as seller / user?-->
Please reach out to [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890).
<!--end expand-->

