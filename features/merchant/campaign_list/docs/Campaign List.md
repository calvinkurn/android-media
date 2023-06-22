---
title: "Campaign List"
labels:
- campaign
- android
- feature
- seller-app
- seller
---


| **Project Lead** |  [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence)  |
| --- | --- |
| Product Manager | Rai Indhira (Graduated) |
| Engineers | [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence) [YOHANN Prananta (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5de4eab04ae7b80d0d19f990?ref=confluence)  |
| Team | Minion Ken  |
| Status | <!--start status:GREEN-->RELEASE<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | <https://docs.google.com/document/d/1QshckTjXeyToE0BW2hQQOpIDZujdhvGsN0fUCMi89vM/edit#>  |
| Module | `features/merchant/campaign_list` |
| Application | Seller App |
| Release | SA 2.87 |

## Table of Contents

<!--toc-->

## Background

Quoting from PRD: Campaign List or Rilisan Spesial had a higher % of not-sold-out campaigns and a smaller % of sold-out campaigns than other campaign types. These data indicate thatthe impact of Rilisan Spesial is still relatively small to the completion rate. So, by enabling the feature on apps hopefully it will help sellers increase traffic and gain more potential customer base through promoting their product launching outside the Tokopedia platform.

## Project Description

The main objective of this project is to create new feature on Seller App which facilitates sellers to monitor their created exclusive launch (or Rilisan Spesial) event.

## Navigation

 

This feature is completed on 2021 and since then it is available for Tokopedia Sellers, but on 2022 the feature entry point currently disabled on Seller App.

This feature entry point is currently remotely disabled by Backend feature flag, so the only way sellers can access the feature is by using desktop site (<https://seller.tokopedia.com/rilisan-spesial>). But, if we, as a developer want to navigate to this feature, we can use the feature applink, located on `sellerapp://campaign-list` . 

## How to

If you want to run this feature (especially if you are using Test App), please make sure you add this feature module as dependency on your Test App build.gradle. 



```
implementation project(rootProject.ext.features.campaignList)
```

## High Level Journey

The project only consist of one page - Campaign List page. Within this page, sellers are able to monitor their previously created exclusive launch event and gives them ability to broadcast the event to their external social media (e.g Whatsapp, Instagram, Line, Facebook, etc) 

![](res/Screen%20Shot%202023-01-09%20at%2015.21.28.png)

![](res/Screen%20Shot%202023-01-09%20at%2015.21.51.png)

![](res/Screen%20Shot%202023-01-09%20at%2015.21.40.png)

## Tech Stack

- Kotlin
- Coroutines
- LiveData
- MVVM architecture
- Dagger
- MockK for unit test dependency mocking
- GraphQL. No web-socket, no gRPC and no bidirectional data stream

## Pages

Here are list of pages that exist within Exclusive Launch (or Rilisan Spesial) feature along with it’s functionalities:



| **Page** | **Functionalities** |
| --- | --- |
| **Campaign List** | View the created exclusive launch campaign |
| Share the campaign to external social media |
| Search exclusive launch campaign by name |
| Apply filter by campaign type |
| Apply filter by campaign status |

## Related Docs



| **Docs Type** | **URL** |
| --- | --- |
| Figma | <https://www.figma.com/file/XvtDOdY0QbwyEuFumhKUJr/%5BRilisan-Spesial%5D-Seller-App-Sharing-Experience?node-id=25%3A13528>  |
| PRD | <https://docs.google.com/document/d/1QshckTjXeyToE0BW2hQQOpIDZujdhvGsN0fUCMi89vM/edit#>  |
| Tech Plan | <https://docs.google.com/document/d/1qLiwKT5V1ZPedgveys4RpW7kyh4N9-qlFjGNCS5rnUk/edit>  |
| GQL |  [GQL - Get Campaign List v2](/wiki/spaces/CAM/pages/694769650/GQL+-+Get+Campaign+List+v2) [Gql GetMerchantCampaignBannerGeneratorData](/wiki/spaces/CAM/pages/1843169882/Gql+GetMerchantCampaignBannerGeneratorData) [GQL - GetSellerCampaignSellerAppMeta](/wiki/spaces/CAM/pages/1840447583/GQL+-+GetSellerCampaignSellerAppMeta)  |

