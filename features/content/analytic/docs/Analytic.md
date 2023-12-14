---
title: "Analytic"
labels:
- analytics
- content
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence) [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 25 Aug 2023 / <!--start status:BLUE-->MA-3.231<!--end status--> <!--start status:GREEN-->SA-2.161<!--end status-->  |
| Module type | <!--start status:PURPLE-->JAVA-LIBRARY<!--end status-->  |
| Module Location | `features.contentAnalytic` | `features/content/analytic` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:15 Sept 2023 (MA-3.234/SA-2.164)-->
Add more constant in Follow Recommendation PR

<https://github.com/tokopedia/android-tokopedia-core/pull/34618>
<!--end expand-->

<!--start expand:25 Aug 2023 (MA-3.231/SA-2.161)-->
First release

<https://github.com/tokopedia/android-tokopedia-core/pull/33992>
<!--end expand-->

## Overview

`Analytic` module under `features/content` directory is a dedicated module for storing all the necessary information related to analytic (constant, helper, etc).

### Background

Content has a lot of module and each module has its own analytic implementation. The analytic implementation across our modules has a lot of similarities since the pattern is almost the same. there’s a lot of same constant defined in each modules and raising confusion among developers. That’s why we create a dedicated module for storing all the common analytic classes for all content’s modules.

### Project Description

`Analytic` is a regular java module and as of now consist of 6 main classes for storing constant:



| **Class** | **Description** | **Example Value** |
| --- | --- | --- |
| BusinessUnit | contains BusinessUnit possible value | - play<br/>- content<br/> |
| CurrentSite | contains CurrentSite possible value | - tokopediamarketplace<br/>- tokopediaseller<br/> |
| Event | contains Event possible value | - openScreen<br/>- clickContent<br/>- viewGroupChat<br/>- …<br/> |
| EventCategory | contains EventCategory possible value | - groupchat room<br/>- unified feed<br/> |
| Key | contains possible analytic key | - event<br/>- eventLabel<br/>- businessUnit<br/>- trackerId<br/>- …<br/> |
| Value | contains possible reusable analytic value | - user<br/>- shop<br/>- follow<br/>- unfollow<br/>- …<br/> |

## How-to

1. Add analytic dependency in your `build.gradle` module



```
dependencies {
    ...
    implementation projectOrAar(rootProject.ext.features.contentAnalytic)
    ...
}
```
2. Use the constant anywhere you want



```
fun onClickCancelSwitchAccount() {
    Tracker.Builder()
        .setEvent(Event.clickContent)
        .setEventAction("click - batal switch account")
        .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
        .setCustomProperty(Key.trackerId, "35508")
        .setEventLabel("$authorId - $authorTypeName")
        .setBusinessUnit(BusinessUnit.content)
        .setCurrentSite(currentSite)
        .setCustomProperty(Key.sessionIris, sessionIris)
        .setUserId(userId)
        .build()
        .send()
}
```



---

## FAQ

<!--start expand:Can module outside content reuse this module?-->
Yes you can if you want attach content analytic in another module (e.g. shop\_page, home, etc). But if it’s not related to content we recommend to not use this module 🙏
<!--end expand-->

