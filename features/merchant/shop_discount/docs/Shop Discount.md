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
[data-colorid=qbfembg2hf]{color:#bf2600} html[data-color-mode=dark] [data-colorid=qbfembg2hf]{color:#ff6640}[data-colorid=jlmp5qn5qw]{color:#bf2600} html[data-color-mode=dark] [data-colorid=jlmp5qn5qw]{color:#ff6640}[data-colorid=nzy8j053gn]{color:#bf2600} html[data-color-mode=dark] [data-colorid=nzy8j053gn]{color:#ff6640}[data-colorid=zgpi2dn8la]{color:#bf2600} html[data-color-mode=dark] [data-colorid=zgpi2dn8la]{color:#ff6640}[data-colorid=pusj228v2p]{color:#bf2600} html[data-color-mode=dark] [data-colorid=pusj228v2p]{color:#ff6640}[data-colorid=w6oskw6fbx]{color:#bf2600} html[data-color-mode=dark] [data-colorid=w6oskw6fbx]{color:#ff6640}[data-colorid=sil0pdobio]{color:#bf2600} html[data-color-mode=dark] [data-colorid=sil0pdobio]{color:#ff6640}[data-colorid=d4qfbsaia1]{color:#bf2600} html[data-color-mode=dark] [data-colorid=d4qfbsaia1]{color:#ff6640}[data-colorid=n9pyroge8g]{color:#bf2600} html[data-color-mode=dark] [data-colorid=n9pyroge8g]{color:#ff6640}[data-colorid=aanf65l4db]{color:#bf2600} html[data-color-mode=dark] [data-colorid=aanf65l4db]{color:#ff6640}[data-colorid=orpxs0sort]{color:#bf2600} html[data-color-mode=dark] [data-colorid=orpxs0sort]{color:#ff6640}*Contents are auto-generated, do not edit. See* [*docs*](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2161051474/) *for details*



| **Project Lead** | [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) |
| --- | --- |
| Product Manager | [Galih Muji Agung](https://tokopedia.atlassian.net/wiki/people/60e29438d6ba9a007766de5a?ref=confluence) |
| Contributors | [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence) [CALVYN JULIAN](https://tokopedia.atlassian.net/wiki/people/5d1adf49cdf26a0d349c76ec?ref=confluence) |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178) |
| Status | <!--start status:GREEN-->RELEASE<!--end status--> |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Product PRD | <https://docs.google.com/document/d/1IVxZS-6kNfAiW9vCWivcx7ugX-Hbw7qjIs2kXcCzIi4/edit> |
| Module | `features/merchant/shop_discount` |
| Release date | <!--start status:GREY-->SA-2.109<!--end status--> |

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

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-05%20at%2017.42.29.png" alt="" />

4. Tap **Diskon Toko**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-05%20at%2017.43.02.png" alt="" />

## How to

If you want to run this feature (especially if you are using Test App), please make sure you add this feature module as dependency on your Test App build.gradle.



```
implementation project(rootProject.ext.features.shopDiscount)

```

## High Level Journey

In essence, it only takes 3 steps for sellers to create a new shop discount for selected products.

1. Landing Page

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-05%20at%2017.48.18.png" alt="" />

2. Pilih Produk Page

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-05%20at%2017.48.43.png" alt="" />

3. Atur Diskon Page (submission)

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-05%20at%2017.49.30.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-01-05%20at%2017.49.47.png" alt="" />

## Pages

Here are list of pages that exist within Diskon Toko feature along with it’s functionalities:



| **Page** | **Functionality** | **Preview** |
| --- | --- | --- |
| **Landing Page**  | Search product by status (1<br/>) | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700538661.png" alt="" /><br/> |
|  | View ongoing discount (2<br/>) |
|  | View upcoming discount (3<br/>) |
|  | View cancelled or deleted discount (4<br/>) |
|  | Bulk delete discount (5<br/>) |
|  | Entry point to discount creation journey (6<br/>) |
|  | Entry point to update discount journey (7<br/>) |
|  | Entry point to shop info and shop remaining discount quota bottomsheet (8<br/>) |
|  | Delete Discount (9<br/>) | '<img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700538661.png" alt="" /><br/><img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700540829.png" alt="" /><br/> |
| **Pilih Produk Page**  | Search product by name or SKU (1<br/>) | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700539319.png" alt="" /><br/> |
|  | Select products to make it discounted (2<br/>) |
| **Atur Diskon Page** | Manage discount for the selected product and it’s variants. For entry point refer number 1 | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700541249.png" alt="" /><br/><img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700541162.png" alt="" /><br/> |
|  | Bulk apply discount for all the selected products and it’s variants. Entry point number 2 | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700541249.png" alt="" /><br/><img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700541497.png" alt="" /><br/> |
|  | Delete product from discount current selection | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700541249.png" alt="" /><br/> |
| Ubah Diskon | Manage discount for the selected product and it’s variants. For entry point refer number 2 | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700542101.png" alt="" /><br/><img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700542096.png" alt="" /><br/> |
|  | Bulk apply discount for all the selected products and it’s variants. Entry point number 1 | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700542101.png" alt="" /><br/><img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700542324.png" alt="" /><br/> |
|  | Delete product from discount current selection (3) | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700542101.png" alt="" /><br/> |
|  |  |  |
| **Search Product** | Search product by name for the selected status (Upcoming, Ongoing, Finished/Cancelled). You need click search (number 1) and you will product search page | <img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700538661.png" alt="" /><br/><img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1700542462.png" alt="" /><br/> |

## Useful Links



| **Docs Type** | **URL** |
| --- | --- |
| GQL | [Slash Price GQL](/wiki/spaces/CAM/pages/1109787226/Slash+Price+GQL) |
| Figma | <https://www.figma.com/file/3c9dJ7nD4FLZnACiwYmSr2/%5BUI%2FUX-M%5D-Slash-Price-at-Seller-App?node-id=3%3A15120> |
| PRD | <https://docs.google.com/document/d/1IVxZS-6kNfAiW9vCWivcx7ugX-Hbw7qjIs2kXcCzIi4/edit> |
| Tech Plan | <https://docs.google.com/document/d/1Wvj6EVxFSDo4w7yO_gLlyhP2IL0cSeVeoMSz1aQTd0E/edit> |

