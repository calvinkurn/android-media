---
title: "Create Post Common"
labels:
- feed
- ccp
- createpost
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)[Shruti Agarwal (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/608fdbe3c87b550069a00200?ref=confluence)[Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)[Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence) |
| Product Manager | - |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 06 Aug 2019 / <!--start status:GREY-->MA-3.38<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [PRD - Initial Content Creation Assessment](/wiki/spaces/CN/pages/1166182376/PRD+-+Initial+Content+Creation+Assessment)[Seller Content Creation Revamp (Product Tagging) on Main App - 210705](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1448682105/Seller+Content+Creation+Revamp+Product+Tagging+on+Main+App+-+210705)[Content Creation for Non-seller (UGC) - 220127](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1894983237/Content+Creation+for+Non-seller+UGC+-+220127) |
| Module Location | `features/content/createpost_common` |

## Table of Contents

<!--toc-->

## Overview

Create Post Common is a common module that used to create a post that will be shown in Feed tab on Homepage and in Feed tab on Shop Page also User Profile.

### Background

The more customers understand about the product, the more chance for them to purchase the product. Feed post is one of the possible solution that help user/seller to promote their product by uploading photos or videos of their products. Create post feed allow user to showcase & promote their products in graphic + caption text form. Because some of the API, components, etc used by multiple modules, so we separate them to a “common” module.

### Project Description

Create post common is a common module that contains the components and all of the functionality for feature where sellers / users are able to post an image, video and carousel images to promote their own products or even share anything that will attract other users interest to check on their shop or profile. This feature is available on both MainApp & SellerApp. To create a post, sellers / users can access it from Feed on Homepage or Shop Page.

Within this module, we have 3 main things:

1. Service for uploading feed post  
Start `SubmitPostService` service for uploading feed post and provide the necessary dependency through dagger.
2. Use case for uploading feed post.  
It has `SubmitPostUseCase` for submitting the feed post meta data & `UploadMultipleMediaUseCase` for uploading the post media (both image & video)
3. Analytics  
It holds image picker insta & create post screen tracker.

## Tech Stack

- **Dynamic Features** **(On-Demand & Install-Time)**: not all users will use this feature. So to save the size of Tokopedia Apps in user’s mobile device, we setup this features as DF On-Demand & DF Install Time for Android device with SDK between 21 - 23. [Click here to see the official documentation.](https://developer.android.com/guide/playcore/feature-delivery)

## GQL List



| **GQL Name** | **Documentation** | **Description** |
| --- | --- | --- |
| Feed Content Submit | [[CVP][GQL] Submit Content (feed\_content\_submit)](/wiki/spaces/CN/pages/1856438703)  | This GQL Query is used to submit/post a content. |
| Upload Multiple Media Image | [Mediauploader: Unified Image and Video uploader services](/wiki/spaces/PA/pages/1417937996/Mediauploader%3A+Unified+Image+and+Video+uploader+services)  | This Use Case is a wrapper for Upload Image and Upload Video |

## FAQ

<!--start expand:What can we use to post a content?-->
Actually you can directly use the Usecase and handle it manually in your page. But if you want to implement it in the background with service and let this module handle it for you, so you only need to handle for success/fail/retry, you can use the `SubmitPostService` .
<!--end expand-->

<!--start expand:Can I get the path of the content media based on the Uri?-->
Yes, you can use the `FileUtil` provided in this module.
<!--end expand-->

<!--start expand:Who should I contact if I want to know more about uploading content?-->
Please reach out to Minion Lance ([Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) ([Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence) )
<!--end expand-->

