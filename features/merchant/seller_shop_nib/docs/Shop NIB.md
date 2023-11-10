---
title: "Shop NIB"
labels:
- feature
- seller
- nib
- seller-app
- shop
- android
- nomor-induk-berusaha
---


| **Project Lead** | [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) |
| --- | --- |
| Product Manager | [Bradley Gunawan](https://tokopedia.atlassian.net/wiki/people/60d023f018e9f60071fa77ce?ref=confluence)  |
| Contributors | [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence) |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178) |
| Status | <!--start status:GREEN-->RELEASE<!--end status--> |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Product PRD | <https://docs.google.com/document/d/1zJzOO-GrX9uXHIZd-5r4YfPAQCE8-45iH_FXNf266Ik/edit#heading=h.7vgmhf4v7kbx>  |
| Module | `features/merchant/seller_shop_nib` |
| Release date | SA-2.153 |

## Table of Contents

<!--toc-->

## Background

Quoting from project PRD: Tokopedia would like to support the government's initiative of increasing the adoption of NIB (Nomor Induk Berusaha) as the legal identification for businesses in Indonesia, including SMEs. On top of that, NIB will also be useful for GoTo for future KYB (KYC for business) implementation, which is now being planned on GoTo level and aimed to be implemented in H2 2023 or H1 2024.

## Project Description

The main objective of this project are to achieve these points:

1. Support government regulations as a partner
2. Regulation compliance (since NIB is actually required by the government)
3. Beneficial for sellers as well (for halal/BPOM certifications, SNI, business loans, etc.)
4. One of the foundations for KYB legalities
5. Enable sellers to submit their NIB to our system

## How to

#### Run using TestApp

If you want to run this feature (especially if you are using Test App), please make sure you add this feature module as dependency on your Test App build.gradle.



```
implementation project(rootProject.ext.features.sellerShopNib)
```

#### Navigation

There are 2 ways to jump to the feature: navigation via applink and navigation via normal journey

##### Via Applink

To access this feature directly, you can use this applink:



```
sellerapp://shop-nib
```

##### Via Normal Journey

Or if you have Seller App installed on your device, you can also follow these steps:

1. Login to SellerApp using a seller account.  
*Notes:* *Use account that hasn’t previously submit NIB file*
2. Navigate to **Home** menu
3. Tap **Masukkan NIB-mu dulu, yuk!** card

![](res/Screenshot%202023-06-12%20at%2009.57.01.png)

## Feature Journey

Here are list of pages that exist within Shop NIB feature:



| **Screenshot** | **Page Name** | **Description** |
| --- | --- | --- |
| ![](res/Screenshot%202023-06-12%20at%2009.35.39.png)<br/> | **Submission Page**(state: Default state) | This is default state of submission page where seller has not filled out any information yet. Button “Selesai” is disabled until the required NIB file and NIB submission date is filled. |
| ![](res/Screenshot%202023-06-12%20at%2009.35.56.png)<br/> | **Submission Page** (State: File selected) | Once seller has select their NIB file from their device storage, the preview will be updated with the selected file. Rules:- Only pdf/jpeg/png file are allowed<br/>- Max file size is 5 MB<br/> |
| ![](res/Screenshot%202023-06-12%20at%2009.36.24.png)<br/> | **Submission Page** (State: Selecting NIB submission date) | After file is selected, seller are also required to fill NIB submission date (Tanggal NIB diterbitkan) |
| ![](res/Screenshot%202023-06-12%20at%2009.36.38.png)<br/> | **Submission Page** (State: File and Date are selected) | After NIB file and NIB submission date are filled, button Selesai are enabled and seller can proceed to upload their NIB. |
| ![](res/Screenshot%202023-06-12%20at%2009.37.20.png)<br/> | **Submission Success Page** | When all the input are valid and successfully uploaded, seller will be redirected to submission success page. Seller can tap “Kembali ke Home” button where they will be redirected back to seller home page. |
| ![](res/Screenshot%202023-06-12%20at%2009.39.46.png)<br/> | **NIB already registered Page** | In case seller are revisit the NIB page again after previous NIB submission done, they will see this warning page. This page informs seller that they can not re-submit the NIB file for the second time. |

## Networking

This feature use combination of GQL and REST API to do its network call.

#### GQL

`sellerSubmitNIBStatus` GQL call is used to check whether seller has previously submit NIB file or not. In case seller has submit NIB file previously, seller are restricted to upload NIB file for the second time.

#### REST API

Other than GQL call, there is REST API call involved to cater upload file mechanism since uploading file is not supported on GraphQL. REST API call is used to upload NIB file along with NIB file submission date.



| **Base URL** | **Endpoint** | **Environment** |
| --- | --- | --- |
| <https://api-staging.tokopedia.com/merchant/toko/> | seller/nib | Staging |
| <https://api.tokopedia.com/merchant/toko/> | Production |

 The request is HTTP POST Multipart and there are 2 required parameters:

**Endpoint**

<https://api.tokopedia.com/merchant/toko/seller/nib> 

**Request**



| **Parameter name** | **Data Type** | **Description** |
| --- | --- | --- |
| *file\_upload* | Byte | We put NIB file bytes here. Max file size is 5MB |
| *release\_date* | String | We put the value from date picker here. The **release\_date** value that we send should use “yyyy-MM-dd” format. e.g: “2023-06-06” |

**Response**



| **Result** | **Response** | **Description** |
| --- | --- | --- |
| **Success** | { "header":{ "process\_time":0.078813746, "messages":null, "reason":"", "error\_code":"200" }, "data":{ "result\_status":{ "code":"200", "message":[ "success" ] }, "status":"" }} | This is the JSON response that we got when NIB file submission success. HTTP code will be **200** |
| **Error** | { "header":{ "process\_time":0.078813746, "messages":null, "reason":"", "error\_code":"400" }, "data":{ "result\_status":{ "code":"400", "message":[ "user is not owner" ] }, "status":"user is not owner" }} | If NIB file submission error because the logged in account is not shop owner, we will get HTTP code **400** and *user is not owner* as the response. |

## Tech Stack

- Kotlin
- StateFlow
- Coroutine
- MocKk
- GQL
- REST API

## Useful Links



| **Docs Type** | **URL** |
| --- | --- |
| GQL | [NIB Project](/wiki/spaces/MC/pages/2194443955/NIB+Project)  |
| Figma | <https://www.figma.com/file/rIo7Mq1n4MKVL9pSqTn250/NIB-%26-OSS-Intergration?node-id=1673-107931&t=iPPlzk8IuPgdN2Ii-0>  |
| PRD | <https://docs.google.com/document/d/1zJzOO-GrX9uXHIZd-5r4YfPAQCE8-45iH_FXNf266Ik/edit#heading=h.7vgmhf4v7kbx>  |
| Tech Plan | <https://docs.google.com/document/d/1zJzOO-GrX9uXHIZd-5r4YfPAQCE8-45iH_FXNf266Ik/edit#heading=h.eluek9wqo38b>  |
| Android Task Breakdown | <https://docs.google.com/spreadsheets/d/1k10NuRIz1sI0BtSkZoMy6mzJ_TC9-hQ5oqcjWMckOwE/edit#gid=0>  |
| API Docs | REST API: <https://tokopedia.atlassian.net/wiki/spaces/MC/pages/2222132284/Shop+NIB+API#Upload-NIB> GQL: <https://tokopedia.atlassian.net/wiki/spaces/MC/pages/2221968965/Shop+NIB+Query#Shop-NIB-status>  |

## FAQ

<!--start expand:Is there any user account that I can use to access the feature?-->
You can use any account to access the feature. But, there are few things to note:

1. Once you successfully submit NIB file, you cannot use it again for the second time. In case
2. Only shop owner account are allowed to upload NIB file, shop admin are restricted to upload NIB file.
<!--end expand-->

