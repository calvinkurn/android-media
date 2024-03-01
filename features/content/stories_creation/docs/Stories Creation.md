---
title: "Stories Creation"
labels:
- stories
- content
- creation
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence)  |
| Product Manager | [Puspacinantya .](https://tokopedia.atlassian.net/wiki/people/6212c45197d313006ba3b24f?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 15 Dec 2023 / <!--start status:GREY-->MA-3.247<!--end status--> <!--start status:GREY-->SA-2.177<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features.storiesCreation` | `features/content/stories_creation` |
| PRD | [PRD - Manual Stories (Organic Shop Updates)](/wiki/spaces/CN/pages/2306803456)  |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:5 Jan 2024 (MA-3.248 / SA-2.178)-->
Entry Point Wording Adjustment, Upload requestId, and Reminder to Open Content from MA (only in SA)

<https://github.com/tokopedia/android-tokopedia-core/pull/36372>
<!--end expand-->

<!--start expand:15 Dec 2023 (MA-3.247 / SA-2.177)-->
First Release  
<https://github.com/tokopedia/android-tokopedia-core/pull/36169>
<!--end expand-->

## Overview

`Stories Creation` is a module owned by Content Marketing Platform (CMP) team to allowing users generate a stories content within Tokopedia App. Only whitelisted users are eligible to access this type of creation, for now it’s only for OS shop. This module covers end-to-end journey of stories creation, starting from media selection & editing, product tagging if any, and upload.

### Background

Short-duration content, or we called it as `stories` , has become a trend nowadays. Almost every social media has this feature to increase user engagement within the app. The key benefit of stories is simplicity, It doesn’t require much effort to create a stories content compared to another type like shorts video, normal post, or livestream. Hence, we hope that many users get motivated to create more contents inside our app.

### Project Description

`Stories creation` has 3 main flows:

- **Media Selection & Editing**  
This process is handled by **mediapicker**, **mediaeditor**, and **universal\_editor** owned by Media team. This process allows user to select 1 image / video from their gallery or taking a fresh new image / video & edit the selected media as well. The editing process has rollence so it can be switched to new / old editor based on needs, but the default is new editor.  


<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231208-114102.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231208-114123.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231208-114214.png" alt="" />
- **Preparation**  
This process will display the selected media & some setup menu. For now, it only has 1 menu to tagging a product & the information of stories duration alive. For more information about product tagging, please refer to this [Product Picker Module Docs](/wiki/spaces/PA/pages/2428577687/Product+Picker).  


<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231212-041805.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231212-041818.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231212-041843.png" alt="" />
- **Upload**  
This process is actually not taking place in `stories_creation` module, instead it’s located in `creation_common` . This module only call a function to put the stories into queue. For more details, you can refer to [Creation Common module documentation.](/wiki/spaces/PA/pages/2443477866/Creation+Common)

## Tech Stack

- [**Jetpack Compose**](https://developer.android.com/jetpack/compose) : A new way to construct UI using declarative approach. With Jetpack Compose, we don’t need to create XML layout file anymore, instead all the UI is written inside kotlin file.
- [**Dynamic Features (On-Demand & Install-Time)**](https://developer.android.com/guide/playcore/feature-delivery) : not all users will use this feature. So to save some APK size, we setup this features as DF On-Demand on MainApp & DF Install-Time on SellerApp.
- **Repository** : a new layer between ViewModel & UseCase. The advantages:


	- Easier to mock network since we only need to mock the repository instead of the usecases.
	- ViewModel constructor won’t be bloated by UseCases anymore.
	- As a place to translate DTO to UI Model.
- **MVI** : we haven’t implemented a full MVI yet, but we are trying to make it MVI. Any user events will be passed to ViewModel with only 1 entry point and the result will be emitted by only 1 UI State, so the pipeline flow is clear and easier to understand.
- [**Flow**](https://developer.android.com/kotlin/flow) : previously we are using LiveData, but for new development we start using Flow (StateFlow & SharedFlow). Flow has a lot of useful operator than LiveData, and it’s suitable for our usecase where we combine all individual state to 1 UI state only.
- [**Fragment Factory**](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory) : Most of the newer features already adopted Fragment Factory. With Fragment Factory, we are able to inject the dependency directly to Fragment constructor.
- [**Adapter Delegate**](/wiki/spaces/PA/pages/697903385/Adapter+Delegate) : We are using Adapter Delegate as replacement to Visitable Pattern. It’s part of our internal library and it’s already used DiffUtil under the hood.

## Flow Diagram

Stories Creation Initial Logic - Diagram.drawio-20240117-094002.png

<img src="https://docs-android.tokopedia.net/images/docs/res/Stories%20Creation%20-%20Preparation.drawio-20231207-055800.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/Stories%20Creation%20-%20Upload.drawio-20231207-062948.png" alt="" />

Stories Creation - Summary Flow.drawio-20240117-093617.png

## Navigation

Mainly, the applink is coming from BE `tokopedia://stories/creation`, but if you want to navigate it manually, you can use this code snippet:



```
fun navigateToStoriesCreation() {
    RouteManager.route(ApplinkConst.Stories.STORIES_CREATION)
}
```

## How-to

This page is only accessible through applink, please refer to [Navigation](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2371715250/Stories+Creation#Navigation) section.



---

## Action Items

- No action items for now.

## Useful Links

- [Development Docs (PRD, Figma, Thanos, Slack Discussion, etc)](/wiki/spaces/PA/pages/2371715275/Phase+1+-+Development+Docs)
- [Creation Common Module Docs](/wiki/spaces/PA/pages/2443477866/Creation+Common)
- [Product Picker Module Docs](/wiki/spaces/PA/pages/2428577687/Product+Picker)

## FAQ

<!--start expand:The media output (video & image) resolution is different if I'm using old editor and new editor. Is it expected?-->
Yes, old editor & new editor have a different behaviour. The new editor has made sure that the media, both image & video, has 9:16 resolution. While in old editor, the size & resolution is following the original setup.
<!--end expand-->

<!--start expand:I can't use new editor in staging environment except I put the rollence manually from Developer Options.-->
Yes that’s expected since there’s no rollence key for using new editor in staging environment.
<!--end expand-->

