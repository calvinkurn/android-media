---
title: "Shop Discount"
labels:
- seller
- shop
- discount
- android
- seller-app
- campaign
- slash-price
- feature
---


| **Project Lead** |  [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence)  |
| --- | --- |
| Product Manager | [Galih Muji Agung](https://tokopedia.atlassian.net/wiki/people/60e29438d6ba9a007766de5a?ref=confluence)  |
| Contributors<br/> | [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence) [CALVYN JULIAN](https://tokopedia.atlassian.net/wiki/people/5d1adf49cdf26a0d349c76ec?ref=confluence) |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178) |
| Status | <!--start status:GREEN-->RELEASE<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | <https://docs.google.com/document/d/1IVxZS-6kNfAiW9vCWivcx7ugX-Hbw7qjIs2kXcCzIi4/edit>  |
| Module | `features/merchant/shop_discount` |
| Release date<br/> | <!--start status:GREY-->SA-2.109<!--end status-->  |

## Table of Contents

<!--toc-->

## Background

Quoting from the project PRD: 75% of active sellers outside Jabodetabek use Android with average time spent of 20 mins vs 13 mins on the Desktop, therefore product team aims to add new promotion tools in Seller App, called Slash Price or Diskon Toko. 

## Project Description

The main objective of this project is to enable seller to access Slash Price or Diskon Toko feature on Seller App. Previously, seller can only access this feature only on Desktop, Minion Ken campaign team are tasked to do some parity and implement this feature as a new feature on Seller App.

## Navigation

There are 2 ways to jump to navigate to the feature: navigation via applink and navigation via normal journey

### Via Applink

To access this feature directly, you can use `sellerapp://shop-discount` applink 

### Via Normal Journey

Or if you have Seller App installed on your device, you can also follow these steps:

1. Login to SellerApp using a seller account.   
*If you don’t have any, you can check this sheet* <https://docs.google.com/spreadsheets/d/1k6AXYKOajj8cvMzqEw-GHQvTpLkPBoRrsIvn7p_pmRk/edit?pli=1#gid=1666413105&range=A1:D6> *to logged in using account provided by QA/TE*
2. Navigate to **Lainnya** bottom navigation menu
3. Tap **Iklan & Promosi Toko**

![](res/Screen%20Shot%202023-01-05%20at%2017.42.29.png)

4. Tap **Diskon Toko**

![](res/Screen%20Shot%202023-01-05%20at%2017.43.02.png)

## How to

If you want to run this feature (especially if you are using Test App), please make sure you add this feature module as dependency on your Test App build.gradle. 



```
implementation project(rootProject.ext.features.shopDiscount)
```

## High Level Journey

In essence, it only takes 3 steps for sellers to create a new shop discount for selected products.

1. Landing Page
2. Pilih Produk Page
3. Atur Diskon Page (submission)

![](res/Screen%20Shot%202023-01-05%20at%2017.48.18.png)

![](res/Screen%20Shot%202023-01-05%20at%2017.48.43.png)

![](res/Screen%20Shot%202023-01-05%20at%2017.49.30.png)

![](res/Screen%20Shot%202023-01-05%20at%2017.49.47.png)

## Pages

Here are list of pages that exist within Diskon Toko feature along with it’s functionalities:



| **Page** | **Functionalities** |
| --- | --- |
| **Landing Page** | Search product by name |
| View ongoing discount |
| View upcoming discount |
| View cancelled or deleted discount |
| Bulk delete discount |
| Entry point to discount creation journey |
| Entry point to update discount journey |
| Entry point to shop info and shop remaining discount quota bottomsheet |
| **Pilih Produk Page** | Search product by name or SKU |
| Select products to make it discounted |
| **Atur Diskon Page** | Manage discount for the selected product and it’s variants |
| Bulk apply discount for all the selected products and it’s variants |
| Delete product from discount current selection |
| Create discount for the selected products |
| **Search Product** | Search product by name for the selected status (Upcoming, Ongoing, Finished/Cancelled) |

## Useful Links



| **Docs Type** | **URL** |
| --- | --- |
| GQL | [Slash Price GQL](/wiki/spaces/CAM/pages/1109787226/Slash+Price+GQL)  |
| Figma | <https://www.figma.com/file/3c9dJ7nD4FLZnACiwYmSr2/%5BUI%2FUX-M%5D-Slash-Price-at-Seller-App?node-id=3%3A15120>  |
| PRD | <https://docs.google.com/document/d/1IVxZS-6kNfAiW9vCWivcx7ugX-Hbw7qjIs2kXcCzIi4/edit>  |
| Tech Plan | <https://docs.google.com/document/d/1Wvj6EVxFSDo4w7yO_gLlyhP2IL0cSeVeoMSz1aQTd0E/edit>  |

