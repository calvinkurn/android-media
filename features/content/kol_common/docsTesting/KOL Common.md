---
title: "KOL Common"
---


| **Status** | <!--start status:RED-->DEPRECATED<!--end status-->  |
| --- | --- |
| **Deadline** | Q2 2023 |
| **Contributors** |  [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)  |
| Product Manager |  - |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 03 Oct 2018 / <!--start status:GREY-->MA-3.1<!--end status-->  |
| Module type | <!--start status:PURPLE-->FEATURE<!--end status-->  |
| Product PRD | - |
| Module Location | `features.kolCommon` | `features/content/kol_common` |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:March 31, 2023 (SA-2.144, MA-3.214)-->
[AN-48502](https://tokopedia.atlassian.net/browse/AN-48502)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:Dec 13, 2022 (MA-3.202)-->
[AN-44374](https://tokopedia.atlassian.net/browse/AN-44374)
 -
 Getting issue details...

STATUS








 [AN-44301](https://tokopedia.atlassian.net/browse/AN-44301)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:Oct 14, 2022 (MA-3.196)-->
[AN-37908](https://tokopedia.atlassian.net/browse/AN-37908)
 -
 Getting issue details...

STATUS




  


 [AN-40152](https://tokopedia.atlassian.net/browse/AN-40152)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:Sept 5, 2022 (MA-3.189)-->
[AN-35897](https://tokopedia.atlassian.net/browse/AN-35897)
 -
 Getting issue details...

STATUS
<!--end expand-->

## Overview

The KOL common purpose is to store any feed content-related use cases that can be used anywhere. However, we now have the [content\_common](/wiki/spaces/PA/pages/2158464917/Content+Common) module because Play & Feed are now starting to be unified. 

Now, we are still in the process of removing unused classes in this module to at least reduce the size of the app.

### Background

The idea behind creating this module is that we want users to be able to see seller-generated content on the Shop page. Because it looks almost the same as the Update tab on the Feed page, we decided to create this common module to store any classes that can be used together on the Shop and Feed pages.

(source: <https://github.com/tokopedia/android-tokopedia-core/pull/3480> )

### Project Description

Let the past be in the past, and the future project that should be related to this module is revamping content distribution on the Shop Page (source: <https://docs.google.com/spreadsheets/d/1QLpVoRw8l08vqrDxDYY7k6sX4E7HHoS5R-f22XLk_jk/edit?pli=1#gid=403058557>).

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-30%20at%2010.45.00.png" alt="" />

So, it’s either we utilize this module or just delete it.

## Tech Stack

### Healthiness

Kotlin Count **73.08%** (based on [Tokopedia App Statistics](https://docs.google.com/spreadsheets/d/1aJGHic-YMP2eEtZoT5eMqJK5qK4rRdbtYMPkv0yGlas/edit#gid=0) version MA-3.204)

### Dependent Modules



| **Module** | **Class** |
| --- | --- |
| discovery/home |  |
| discovery/search |  |
| merchant/shop\_page |  |
| merchant/feed\_shop | <br/><br/>```<br/>import com.tokopedia.kolcommon.di.KolCommonModule<br/>import com.tokopedia.kolcommon.data.pojo.follow.FollowKol<br/>import com.tokopedia.kolcommon.data.pojo.follow.FollowKolData<br/>import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery<br/>import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase<br/>import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase<br/>import com.tokopedia.kolcommon.view.listener.KolPostLikeListener<br/>import com.tokopedia.kolcommon.util.PostMenuListener<br/>import com.tokopedia.kolcommon.util.createBottomMenu<br/>import com.tokopedia.kolcommon.view.subscriber.LikeKolPostSubscriber<br/>```<br/><br/> |
| content/explore |  |
| content/feed | <br/><br/>```<br/>import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase<br/>import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase<br/>import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase<br/>import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase<br/>import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel<br/>import com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData<br/>import com.tokopedia.kolcommon.view.viewmodel.ViewsKolModel<br/>```<br/><br/> |
| content/kol | <br/><br/>```<br/>import com.tokopedia.kolcommon.util.GraphqlErrorException<br/>import com.tokopedia.kolcommon.util.TimeConverter<br/>import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase<br/>import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase<br/>import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase<br/>import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase<br/>import com.tokopedia.kolcommon.view.listener.KolPostLikeListener<br/>import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery<br/>```<br/><br/> |

 

Note that the 

`kol\_common` module can be removed from 

`discovery/home`, 

`discovery/search`, 

`merchant/shop\_page`, and 

`content/explore` modules.



### Unused Dependencies



```
implementation projectOrAar(rootProject.ext.features.resourcesCommon)
implementation projectOrAar(rootProject.ext.libraries.tkpddesign)
implementation projectOrAar(rootProject.ext.libraries.baseList)
implementation rootProject.ext.unifyDependencies.principles
implementation rootProject.ext.unifyDependencies.components
implementation rootProject.ext.unifyDependencies.icons
implementation rootProject.ext.unifyDependencies.global_error
implementation rootProject.ext.supportLibDependencies.constraintLayout
implementation rootProject.ext.miscDependencies.roundCornerProgressBar
```

 

👆 all of these are safe to be removed from 

`kol\_common/build.gradle`

## Navigation

There is no activity, or fragment in this module. What's left is only the use case, model, and util classes.

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-30%20at%2011.08.31.png" alt="" />

## How-to

Instead of using any of the classes inside this module, I recommend you just move the class you need to your module. And how to do that?

- Cmd + X
- Cmd + V

easy right? 😆

and next, ask for our approval (we’ll automatically approve your PR 🫱🏻‍🫲🏼 ).



---

## Action Items

- Moving anything that's still in use and deleting what's not ([Pull Request: Removal of unused classes and resources](https://github.com/tokopedia/android-tokopedia-core/pull/31414))

## Useful Links

-

[data-colorid=x3hikj59h5]{color:#bf2600} html[data-color-mode=dark] [data-colorid=x3hikj59h5]{color:#ff6640}