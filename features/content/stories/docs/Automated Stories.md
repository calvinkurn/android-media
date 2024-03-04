---
title: "Automated Stories"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence)  |
| Product Manager | [Puspacinantya .](https://tokopedia.atlassian.net/wiki/people/6212c45197d313006ba3b24f?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 09 Oct 2023 / <!--start status:GREY-->MA-3.240<!--end status--> <!--start status:GREY-->SA-2.170<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features.stories` | `features/content/stories` |
| PRD | [PRD - Automated Stories (Shop Updates)](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2260959233/PRD+-+Automated+Stories+Shop+Updates)  |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:09 Oct 2023 (MA-3.240 / SA-2.170)-->
First Release  
<https://github.com/tokopedia/android-tokopedia-core/pull/34881>
<!--end expand-->

<!--start expand:01 Nov 2023 (MA-3.244 / SA-2.174)-->
Enhancement for stories timer when user disabling animation in their device  
<https://github.com/tokopedia/android-tokopedia-core/pull/35888>
<!--end expand-->

<!--start expand:26 Jan 2024 (MA-3.253 / SA-2.183)-->
Fixing image in stories group is stretched  
<https://github.com/tokopedia/android-tokopedia-core/pull/36975>
<!--end expand-->

## Overview

`Stories` is a feature module that allow user to have instagram-story based experience in our Tokopedia App. User are eligible to see the stories entry point for each shop account that have stories. This module cover all stories viewing room related.

### Background

Tokopedia Sellers that we surveyed have been using Instagram to share product updates, while 51% use it to share promotional and discount information through Stories. Meanwhile, in the context of Tokopedia, sellers do not have one simple way to communicate updates to buyers. This needs have been validated by the [RAD research](https://docs.google.com/presentation/d/1UXofhJjqZmk65X4kbYc8_0b7oAlbi9GwrGwT1YvYrHY/edit#slide=id.g22cb024f581_0_0), which mentioned that 51% Tokopedia sellers consider shop updates to be a prominent feature in Tokopedia.

### Project Description

There is several entry point for the stories viewing room, for each entry point we can see the stories badge in shop image, the stories indicator will visible when the shop have un-seen stories for the user.

#### Entry Point

- TopChat

chat_room.png

- TopChatList

chat_list.png

- ShopPage

shop_page.png

#### Stories Viewing Room

[stories\_room.webm](/wiki/download/attachments/2511306857/stories_room.webm?version=1&modificationDate=1708662173055&cacheVersion=1&api=v2&width=320)

Inside stories room, user can see the stories get categorize by its stories group (topic of the auto seller generated content). In the first development phase, stories room can only contains image type content from the auto seller generated content. But within [manual stories creation](/wiki/spaces/PA/pages/2371715250/Stories+Creation) development, user can see the new group called `Updated` belongs with video type is available for the stories viewing room.

## Tech Stack

- [**Jetpack Compose**](https://developer.android.com/jetpack/compose) : A new way to construct UI using declarative approach. With Jetpack Compose, we don’t need to create XML layout file anymore, instead all the UI is written inside kotlin file.
- **Repository** : a new layer between ViewModel & UseCase. The advantages:


	- Easier to mock network since we only need to mock the repository instead of the usecases.
	- ViewModel constructor won’t be bloated by UseCases anymore.
	- As a place to translate DTO to UI Model.
- **MVI** : we haven’t implemented a full MVI yet, but we are trying to make it MVI. Any user events will be passed to ViewModel with only 1 entry point and the result will be emitted by only 1 UI State, so the pipeline flow is clear and easier to understand.
- [**Flow**](https://developer.android.com/kotlin/flow) : previously we are using LiveData, but for new development we start using Flow (StateFlow & SharedFlow). Flow has a lot of useful operator than LiveData, and it’s suitable for our usecase where we combine all individual state to 1 UI state only.
- [**Fragment Factory**](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory) : Most of the newer features already adopted Fragment Factory. With Fragment Factory, we are able to inject the dependency directly to Fragment constructor.
- [**Adapter Delegate**](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/697903385) : We are using Adapter Delegate as replacement to Visitable Pattern. It’s part of our internal library and it’s already used DiffUtil under the hood.

## Flow Diagram

Untitled Diagram.drawio (1).png

## Navigation

Basically we can navigate to stories viewing room using app link, but there is several condition that you need to use the additional parameter inside the app link for it.

Main app link: `tokopedia://stories/{stories_type}/{author_id}`

Additional app link: `tokopedia://{stories_type}/{author_id}?source={source}&source_id={source_id}&entrypoint={entrypoint}`



| stories\_type | `shop` |
| --- | --- |
| author\_id | `shopId` |
| source (additional) | `shop-entrypoint`/ `story-group` / `sharelink` |
| source\_id (additional) | `shopCategoriesId` |
| entrypoint (additional) | `storiesEntryPointPageName` (any prefix should be fine; `topchat` / `shopPage`/ `feed` etc) |

## How-to

This page is only accessible through applink, please refer to [Navigation](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2371715250/Stories+Creation#Navigation) section.

## Action Items

No action items for now.

## Useful Links

- [PRD](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2260959233/PRD+-+Automated+Stories+Shop+Updates)
- [Tech Plan](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2308177963/CVP+Tech+Plan+Automated+Story+-+Shop+Update+Viewer+side)
- [Figma](https://www.figma.com/file/sYnxbgXNJ2XepdKuCyStMj/%5BPlay%5D-Stories-in-Shop?type=design&node-id=227-41996&t=LavNHlv74H0PBuPB-0)
- [[RCA] Stories Viewing Room - Animation Disabled from Devices Settings](https://docs.google.com/document/d/1dGUBtNvQEmROCsbKGxbgWsa_bT9qr8Fw5mMxmSSZIW4/edit#heading=h.tkk8unyu3bhj)

## FAQ

<!--start expand:Is all user is eligible to see stories?-->
Yes, all Tokopedia user (buyer/seller) is eligible to see stories viewing room
<!--end expand-->

<!--start expand:Is all user is eligible to create stories?-->
No, for now only shop (PM Pro & Official Store) is eligible for creating stories by ASGC
<!--end expand-->

<!--start expand:Is stories only support for image content type?-->
In the first development stories only contains image media type. But in [manual stories creation](/wiki/spaces/PA/pages/2371715250/Stories+Creation) development, user can see the new group called `Updated` belongs with video type is available for the stories viewing room.
<!--end expand-->

