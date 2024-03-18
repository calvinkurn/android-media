---
title: "Stories Internal"
labels:
- stories
- content
- internal
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)  |
| Product Manager | [Puspacinantya .](https://tokopedia.atlassian.net/wiki/people/6212c45197d313006ba3b24f?ref=confluence)  |
| Team | [Minion Lance](/wiki/spaces/PA/pages/1939344716/Android+Minion+Lance) |
| Release date | 27 Oct 2023 / <!--start status:BLUE-->MA-3.240<!--end status--> <!--start status:GREEN-->SA-2.170<!--end status--> |
| Module type | <!--start status:BLUE-->SUB-FEATURE<!--end status--> |
| Module Location | `features.storiesInternal` | `features/content/stories_internal` |
| PRD | [PRD - Automated Stories (Shop Updates)](/wiki/spaces/CN/pages/2260959233)  |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:27 Oct 2023 (MA-3.240/SA-2.170)-->
First Release  
<https://github.com/tokopedia/android-tokopedia-core/pull/35540>
<!--end expand-->

## Overview

**Stories Internal** is a internal module for stories feature purposes and should be used only for stories related module. As of now, **stories internal is used to cater data sharing between** `stories widget` **across Tokopedia app & used in** `stories` **module to show / hide coachmark**. Later it might be used for other purposes, but still within stories feature scope for sure.

### Background

Stories is one of CMP tribe feature where users are able to see short content within Tokopedia app that will be unavailable after some period of time. We have viewing stories room to view the content, stories creation, and also stories widget that attached across Tokopedia pages as entry point to viewing stories room. For stories widget, there’s a difference in terms of UI between stories that has been seen and not. To make the UI sync between widgets, we create a global data storage to store all stories status based on user id.

### Project Description

For stories status storage, there’s 2 main functions that are able to use:

1. hasSeenAllAuthorStories  
To validate whether user has seen all the stories from a specific author. Author might be seller / user.
2. setSeenAllAuthorStories  
To set that user has seen all the stories from a specific author.

Beside stories status storage, there’s a stories preference util (StoriesPreferenceUtil.kt) that can be used to store any data related to stories viewing room. For now, it’s only storing stories viewing room coachmark status.

## Tech Stack

- [**DataStore**](https://developer.android.com/topic/libraries/architecture/datastore): Act as a database that cached the stories status based on user id.
- [**Coroutine**](https://developer.android.com/kotlin/coroutines): Used to make sure all the operations are done in a appropriate thread.
- [**Dagger 2**](https://dagger.dev/): Used to inject stories storage module to component

## How-to

Steps for using Stories Status Storage:

1. Add module dependency



```
dependencies {
  ...
  implementation project(rootProject.ext.features.storiesInternal)
}
```
2. Add `StoriesSeenStorageModule` in your Dagger component



```
@Component(
    dependencies = [...],
    modules = [
        ...
        StoriesSeenStorageModule::class,
        ...
    ]
)
interface YourComponent {
  ...
}
```
3. Inject `StoriesSeenStorage` anywhere within your component



```
class YourRepository @Inject constructor(
  private val storiesSeenStorage: StoriesSeenStorage
) {

  fun hasSeenAllAuthorStories(
    shopId: String,
    lastUpdatedTime: Long,
  ) {
    storiesSeenStorage.hasSeenAllAuthorStories(
      StoriesSeenStorage.Author.Shop(shopId),
      lastUpdatedTime,
    )
  }
  
  fun setSeenAllAuthorStories(
    shopId: String,
  ) {
    storiesSeenStorage.setSeenAllAuthorStories(
      StoriesSeenStorage.Author.Shop(shopId)
    )
  }
}
```



---

## Useful Links

- [Stories - PRD](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2260959233/PRD+-+Automated+Stories+Shop+Updates)
- [Stories - Figma](https://www.figma.com/file/sYnxbgXNJ2XepdKuCyStMj/%5BPlay%5D-Story---Mobile?type=design&mode=design&t=4hN9TbJELZw9wRt9-0)

## FAQ

<!--start expand:Can other content-related module depend on stories_internal module?-->
No, stories\_internal is only for stories-related code, not content-related.
<!--end expand-->

