---
title: "Buyer Order Management"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Edwin Nurwansyah](https://tokopedia.atlassian.net/wiki/people/622e71a875f257006a98bab9?ref=confluence) [Ilham .](https://tokopedia.atlassian.net/wiki/people/5de4d2148743750d00b7cc12?ref=confluence) [REIVIN OKTAVIANUS](https://tokopedia.atlassian.net/wiki/people/5dae89dab86cd40c2da5ad2f?ref=confluence) [Rizqi Aryansa](https://tokopedia.atlassian.net/wiki/people/5e25ee87006fae0ca232e1ac?ref=confluence) [Yehezkiel .](https://tokopedia.atlassian.net/wiki/people/5c94aa7a7792242c8613ad14?ref=confluence) [Yusuf Hendrawan](https://tokopedia.atlassian.net/wiki/people/5df336f3f4ab290ecfc64169?ref=confluence)  |
| Product Manager | [Erik Novaldi Sampurno](https://tokopedia.atlassian.net/wiki/people/622e70525b6d710070a01bed?ref=confluence) [Priscilla Christie Suyanto](https://tokopedia.atlassian.net/wiki/people/60865011248ef6007359be97?ref=confluence) [Venessa Jeremia](https://tokopedia.atlassian.net/wiki/people/5ce4e9c983de300fe5d546d7?ref=confluence)  |
| Team | [Minion Stuart](https://tokopedia.atlassian.net/people/team/eeba862a-bd9d-472c-b901-415b15b1a37e?ref=directory&src=peopleMenu) |
| Release date | 21 Jun 2022 / <!--start status:GREY-->MA-3.131<!--end status-->  |
| Module type |  <!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/ordermanagement/buyer_order_detail` |

## Table of Contents

- [Overview](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2138637696/Buyer+Order+Management#%5BhardBreak%5DOverview)
- [Release Notes](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2138637696/Buyer+Order+Management#Release-Notes)
- [Navigation](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2138637696/Buyer+Order+Management#Navigation)
- [Pages](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2138637696/Buyer+Order+Management#Pages)

## Overview

Buyer Order Management, usually called BOM, is a feature that exists only within the CustomerApp that allows buyers to view and manage their physical goods order. The feature includes a page that shows information such as the purchased items, the shipping address, and the total cost, etc. It also provides options for the customer to manage their order, such as canceling the order, tracking the shipment status, etc.

## Release Notes

<!--start expand:10 February 2023 (MA-3.209)-->
###### NOW Schedule DeliveryPR: <https://github.com/tokopedia/android-tokopedia-core/pull/31629>
<!--end expand-->

<!--start expand:8 November 2022 (MA-3.200)-->
###### Adding POD thumbnailPR: <https://github.com/tokopedia/android-tokopedia-core/pull/28806>

###### Adding Insurance Information on BOM DetailPR: <https://github.com/tokopedia/android-tokopedia-core/pull/29500>

###### Refactor Buyer Order Detail ImplementationPR: <https://github.com/tokopedia/android-tokopedia-core/pull/29468>
<!--end expand-->

<!--start expand:4 October 2022 MA-3.195-->
###### Adding Biaya Platform InfoPR: <https://github.com/tokopedia/android-tokopedia-core/pull/29267>

###### Show resolution statusPR: <https://github.com/tokopedia/android-tokopedia-core/pull/28818>
<!--end expand-->

<!--start expand:17 September 2022 (MA-3.191)-->
###### Epharmacy - Integration New ButtonsPR: <https://github.com/tokopedia/android-tokopedia-core/pull/27557>
<!--end expand-->

<!--start expand:MA-3.155-->
###### Order ExtensionPR: <https://github.com/tokopedia/android-tokopedia-core/pull/23300>
<!--end expand-->

## **Navigation**

- Buyer Order Detail Page External AppLink: `tokopedia://marketplace/order/{orderId}`
- Buyer Order Detail Page Internal AppLink: `tokopedia-android-internal://marketplace/buyer-order-detail?order_id={orderId}`
- Buyer Order Extension Page External AppLink: `tokopedia://marketplace/buyer-order-extension?order_id={orderId}`
- Buyer Order Extension Page Internal AppLink: `tokopedia-android-internal://marketplace/buyer-order-extension?order_id={orderId}`

## **Pages**

- [Buyer Order Detail](/wiki/spaces/PA/pages/2151845406/Buyer+Order+Detail)
