---
title: "Feed Tab on Shop Page"
labels:
- shop-page
- content
- feed
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| **Contributors** |  [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence) [M. Ilham Jamaludin](https://tokopedia.atlassian.net/wiki/people/5c87306ea329a40b8555c1ca?ref=confluence)  |
| Product Manager |  [Glorianna Chrysilla](https://tokopedia.atlassian.net/wiki/people/62cb5abf10fcc6f7ae3dc39a?ref=confluence) [Ayila Adzkiya Sucahyo](https://tokopedia.atlassian.net/wiki/people/62856e9d82e85d0068f45b40?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date<br/> | 24 May 2019 / <!--start status:GREY-->MA-3.32<!--end status--> <!--start status:GREY-->SA-1.40.0<!--end status--> |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [Content Distribution](/wiki/spaces/CN/pages/484479231/Content+Distribution)<br/><br/> [HPF-447](https://tokopedia.atlassian.net/browse/HPF-447)<br/> -<br/> Getting issue details...<br/><br/>STATUS<br/><br/><br/><br/> [Ultimate Create Post Flow](/wiki/spaces/CN/pages/484510926/Ultimate+Create+Post+Flow)  |
| Module Location | `features.feedShop` | `features/merchant/feed_shop` |

## Table of Content
s

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:Feb 3, 2023 (MA-3.207, SA-2.137)-->
[AN-45360](https://tokopedia.atlassian.net/browse/AN-45360)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:Jan 6, 2023 (MA-3.203)-->
[AN-43930](https://tokopedia.atlassian.net/browse/AN-43930)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:Dec 16, 2022 (MA-3.202, SA-2.132)-->
[AN-44374](https://tokopedia.atlassian.net/browse/AN-44374)
 -
 Getting issue details...

STATUS
<!--end expand-->

## Overview

The objective of Feed tab on Shop page creation is to increase content distribution channels for created content. 



### Background

With an increase in content supply and no increase in content distribution, many contents remain unseen. This initiative will increase content distribution channels and improve existing ones so that contents will be more evenly distributed. (based on: [Content Distribution](/wiki/spaces/CN/pages/484479231/Content+Distribution) last updated: May 26, 2019

)

### Project Description

Who is it for?

- Tokopedia:


	- So that we will be able to help the CVR of other pages my including contents
	- So that we will be able to urge users to create more with more distribution channels
- Content Creators → So that creators will be able to have better exposure after they create content
- Content Consumers → So that users will be able to receive more variety of content and discover it in more places

The future project is we need to revamp content distribution on the Shop Page:

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-03-20%20at%2016.30.01-20230320-093007.png" alt="" />

Source: <https://docs.google.com/document/d/1IhOZk2jhyVe5h14cL8moXe_9RuElM2HPNuWQXNDXVfI/edit#> 

## Tech Stack

- Healthiness (based on <https://docs.google.com/spreadsheets/d/1aJGHic-YMP2eEtZoT5eMqJK5qK4rRdbtYMPkv0yGlas/edit#gid=0> version MA-3.212) 


	- Kotlin Count 
	
	**100%**
	- Unit Test Coverage **100%**
	- Dark mode Coverage **100%**
	- Still depending on `core_legacy` module ❌
	- Still using the `tkpddesign` components ❌
- MVP design pattern (presentation-level)
- All the components (or widgets) on this page are shared within Feed modules, mostly used components from the feed\_component

 module.
- 4 of the 8 use cases used are deprecated, see the screenshot below:

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-03-20%20at%2017.49.47-20230320-104952.png" alt="" />

## Navigation

### Customer App

→ Open Tokopedia app → Click one of the Shop → Go to Feed tab

[Screen Recording 2023-03-20 at 17.35.07.mov](/wiki/download/attachments/2196282130/Screen%20Recording%202023-03-20%20at%2017.35.07.mov?version=1&modificationDate=1679308643158&cacheVersion=1&api=v2&width=340)

### Seller App

→ Open Seller App → Go to Shop page → Go to Feed tab

[Screen Recording 2023-03-20 at 17.39.42.mov](/wiki/download/attachments/2196282130/Screen%20Recording%202023-03-20%20at%2017.39.42.mov?version=1&modificationDate=1679308834310&cacheVersion=1&api=v2&width=340)

### AppLink

- Public → `tokopedia://shop/{shop_id}/feed`
- Internal → `tokopedia-android-internal://marketplace/shop-page{shop_id}/feed`

[feed-shop-applink-internal.mov](/wiki/download/attachments/2196282130/feed-shop-applink-internal.mov?version=1&modificationDate=1679307918807&cacheVersion=1&api=v2&width=340)

## How-to

### Open the page

→ via Applink use [RouteManager](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1419983437/Applink+Library#How-to-use-library) class. You can also test the AppLink via the [Developer Options](/wiki/spaces/PA/pages/1639001376/How+to+use+Route+Manager) page.



| **Internal AppLink** | **Public AppLink**  | **Required Parameters** |
| --- | --- | --- |
| `tokopedia-android-internal://marketplace/shop-page{shop_id}/feed` | `tokopedia://shop/{shop_id}/feed` | shop\_id → String |

### Compile

→ just choose the `feature:merchant:feed_shop` module from [Switch to Testapp](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1428784251/Testapp+Build+Plugin) menu (please install the Switch to Testapp plugin first, find the plugin on [Testapp Build & Plugin](/wiki/spaces/PA/pages/1428784251)).

To help you further, use the [Find Usage](https://www.jetbrains.com/idea/guide/tips/find-usages/) tool in your Android Studio.



---

## Action Items

- Replace all the deprecated use cases with new ones

## Useful Links

- <https://docs.google.com/document/d/1IhOZk2jhyVe5h14cL8moXe_9RuElM2HPNuWQXNDXVfI/edit#>

[data-colorid=dvrgtihw3f]{color:#36b37e} html[data-color-mode=dark] [data-colorid=dvrgtihw3f]{color:#4cc994}[data-colorid=teewos01zn]{color:#36b37e} html[data-color-mode=dark] [data-colorid=teewos01zn]{color:#4cc994}[data-colorid=qnidmqz2im]{color:#bf2600} html[data-color-mode=dark] [data-colorid=qnidmqz2im]{color:#ff6640}[data-colorid=uzvhc090lg]{color:#36b37e} html[data-color-mode=dark] [data-colorid=uzvhc090lg]{color:#4cc994}[data-colorid=rnknbc1tkx]{color:#ff5630} html[data-color-mode=dark] [data-colorid=rnknbc1tkx]{color:#cf2600}