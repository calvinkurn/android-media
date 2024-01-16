---
title: "Play Broadcaster"
labels:
- broadcaster
- play
- ccp
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| **Contributors** | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)  |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence) [Fadlim . (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/61e501ac98cd6100703021eb?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 01 Oct 2021 <!--start status:BLUE-->MA-3.147<!--end status--> / 31 Jul 2020 <!--start status:GREEN-->SA-2.16<!--end status--> |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [PRD](https://docs.google.com/document/d/1qRGR3mS_6bINejj6otfWF7zuJ9rgcRlLMQ1BeE5FmEw/edit#heading=h.p26s1l4lrtsr) |
| Module Location | `features.playBroadcaster` | `features/content/play_broadcaster` |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:17 Feb 23 (MA-3.208/SA-2.138)-->
1. Fix TnC Text Color on Darkmode  
<https://github.com/tokopedia/android-tokopedia-core/pull/31517>
<!--end expand-->

## Overview

**Play Broadcaster** is a feature that enables seller / user to do livestream on their own. This feature can be used by sellers to promote their products and by users to promote other shops product as affiliate. Play Broadcaster is owned by CMP tribe, especially Content Creation Platform (CCP) team. 

### Background

The more customers understand about the product, the more chance for them to purchase the product. Live streaming is one solution that offers some advantages over the drawbacks of e-commerce. Sellers now are able to showcase & promote their products directly to potential customers lively. The interaction is also happening in 2 ways as customers are able to ask questions in the chat and seller can read & answer it. Live stream is also a good alternative for sellers to keep their sales going during COVID-19 pandemic situation since direct contact & activities are being restricted by government.

### Project Description

Play Broadcaster is a feature where sellers / users that meet the T&C are able to start a live stream to promote their own products / promote other seller products. This feature is available on both MainApp & SellerApp with number of entry points. 

Here is the list of entry points in both MA & SA:



| **MainApp** | **SellerApp** |
| --- | --- |
| Feed | Shop Page |
| User Profile | Search |
|  | Iklan dan Promosi |

Before starting a live stream, users need to setting up the live stream informations:

- Title (mandatory)
- Cover (mandatory)
- Tagged products
- Schedule (if applicable)

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20221213-101023.png" alt="" />

During live streaming, users are able to:

- change tagged product
- pin message
- pin product
- create engagement (giveaway / quiz)

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20221213-101233.png" alt="" />

After live stream, users are able to see live reports & transcode their live stream session to VOD so it can be replayed. 

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20221213-101316.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20221213-101332.png" alt="" />

## Tech Stack

- **WebSocket** : a real-time technology to make sure all the interactions made by streamer & viewer are updated lively. Chat, tagged product, pinned message, are the few interactions that are updated real time. [Click here to see the official documentation.](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/)
- **Larix** : a 3rd library used to push camera output frame to server. Larix already accomodates all the things that we need for livestreaming and it’s free. [Click here to see the official documentation.](https://softvelum.com/larix/android_sdk/)
- **Dynamic Features (On-Demand & Install-Time)** : not all users will use this feature. So to save some APK size, we setup this features as DF On-Demand on MainApp & DF Install-Time on SellerApp. [Click here to see the official documentation](https://developer.android.com/guide/playcore/feature-delivery)
- **Repository** : a new layer between ViewModel & UseCase. The advantages:


	- Easier to mock network since we only need to mock the repository instead of the usecases.
	- ViewModel constructor won’t be bloated by UseCases anymore.
	- As a place to translate DTO to UI Model.
- **MVI** : we haven’t implemented a full MVI yet, but we are trying to make it MVI. Any user events will be passed to ViewModel with only 1 entry point and the result will be emitted by only 1 UI State, so the pipeline flow is clear and easier to understand.
- **Flow** : previously we are using LiveData, but for new development we start using Flow (StateFlow & SharedFlow). Flow has a lot of useful operator than LiveData, and it’s suitable for our usecase where we combine all individual state to 1 UI state only. [Click here to see the official documentation.](https://developer.android.com/kotlin/flow)
- **Fragment Factory** : Most of the newer features already adopted Fragment Factory. With Fragment Factory, we are able to inject the dependency directly to Fragment constructor. [Click here to see the official documentation.](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory)
- **Adapter Delegate** : We are using Adapter Delegate as replacement to Visitable Pattern. It’s part of our internal library and it’s already used DiffUtil under the hood. [Click here to see the official documentation.](/wiki/spaces/PA/pages/697903385/Adapter+Delegate)
- **View Component** : it’s one of our Play internal library. View Component is used to delegate view logic into 1 dedicated class, so our activity / fragment won’t be bloated by view logics.

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/Play%20Broadcaster.drawio-20221214-073903.png" alt="" />

## Navigation

[Play Broadcaster Entry Point - Feed.mov](/wiki/download/attachments/914920148/Play%20Broadcaster%20Entry%20Point%20-%20Feed.mov?version=1&modificationDate=1671007827801&cacheVersion=1&api=v2&width=340)

[Play Broadcaster Entry Point - User Profile.mov](/wiki/download/attachments/914920148/Play%20Broadcaster%20Entry%20Point%20-%20User%20Profile.mov?version=1&modificationDate=1671007884239&cacheVersion=1&api=v2)

[Screen Recording 2022-12-14 at 15.47.26.mov](/wiki/download/attachments/914920148/Screen%20Recording%202022-12-14%20at%2015.47.26.mov?version=1&modificationDate=1671007921693&cacheVersion=1&api=v2)

[Screen Recording 2022-12-14 at 15.47.02.mov](/wiki/download/attachments/914920148/Screen%20Recording%202022-12-14%20at%2015.47.02.mov?version=2&modificationDate=1671007944752&cacheVersion=1&api=v2&width=340)

[Screen Recording 2022-12-14 at 15.55.56.mov](/wiki/download/attachments/914920148/Screen%20Recording%202022-12-14%20at%2015.55.56.mov?version=1&modificationDate=1671008190261&cacheVersion=1&api=v2&width=340)

## How-to

To open Play Broadcaster without any parameter, all you need to do is using `RouteManager.route()` and use `ApplinkConst.PLAY_BROADCASTER` as the applink.



```
fun openPlayBroadcaster() {
    RouteManager.route(requireContext(), ApplinkConst.PLAY_BROADCASTER)
}
```

But if you need to send some parameters, you need to add dependency to `content_common` as all the parameter field are stored in this module.



```
// your module build.gradle
dependencies {
    ...
    implementation projectOrAar(rootProject.ext.features.contentCommon)
    ...
}

// your code
fun openPlayBroacaster() {
    val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PLAY_BROADCASTER)
    intent.putExtra(KEY_AUTHOR_TYPE, TYPE_USER)
    startActivity(intent)
}
```

Here is the list of parameter that you can use:



| **Parameter** | **Description** | **Key** | **Value** |
| --- | --- | --- | --- |
| Author Type | to determine which author is preferred. If you don’t provide this, the last used account will be selected. | author\_type(or you can use `ContentCommonUserType.KEY_AUTHOR_TYPE` ) | - content-user<br/>- content-shop<br/><br/>(or you can use `ContentCommonUserType.TYPE_USER` || `ContentCommonUserType.TYPE_SHOP` |



---

## Action Items

- Create instrumentation test for generating resources Id
- Increase unit test to at least 80%

## Useful Links

- [PRD](https://docs.google.com/document/d/1qRGR3mS_6bINejj6otfWF7zuJ9rgcRlLMQ1BeE5FmEw/edit#heading=h.p26s1l4lrtsr)
- [Timeline Gantt Chart](https://docs.google.com/spreadsheets/d/1l8WUN2e69PuxRymo6Ot3SlCFZyn32O9Ai4m0lbJLaF0/edit#gid=460206726)
- [Figma](https://www.figma.com/file/A5kwO0nMJoHCFIEGaqACqA/%5BUI-M-Play%5D-SGC-New-Concept?node-id=485%3A13003)
- [DRD](https://tokopedia.atlassian.net/wiki/spaces/PD/pages/795710334/Play+-+Seller+Generated+Content)
- [Micro Interaction](https://tokopedia.atlassian.net/wiki/spaces/PD/pages/800719204/Play+SGC+-+Phase+1)

## FAQ

<!--start expand:Why my seller / user account can't see live stream entry points?-->
Seller account can’t see live stream entry points because:

1. Your shop is not PM / PM Pro / OS
2. Your shop got banned

User account can’t see live stream entry points because:

1. Your account is not whitelisted yet
2. Your account is got banned
3. Your MainApp application version is <!--start status:BLUE-->MA-3.181<!--end status--> or older.
<!--end expand-->

<!--start expand:Can I live stream on emulator?-->
Yes you can, setup your emulator camera either Emulated / Webcam / Virtual Screen.
<!--end expand-->

<!--start expand:What testing account that I can use for doing live stream as seller / user?-->
Please reach out to [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890).
<!--end expand-->

<!--start expand:Can I pause the live stream?-->
Yes, live stream will be paused if you leave Live Room by pressing home button / kill app. But please note that **we have paused duration set up in backend, if you pause your live stream longer than the threshold, it will end your live stream automatically.**
<!--end expand-->

<!--start expand:Can 1 account do multiple live stream at the same time?-->
No, out live stream system only **allows 1 live stream session for particular account**. But if you have eligible seller & user to live stream, **you can live stream as both seller and user at the same time on different devices.**
<!--end expand-->

