---
title: "Seller Order Management"
labels:
- seller
- sellerorder
- physical-goods
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Edwin Nurwansyah](https://tokopedia.atlassian.net/wiki/people/622e71a875f257006a98bab9?ref=confluence) [Ilham .](https://tokopedia.atlassian.net/wiki/people/5de4d2148743750d00b7cc12?ref=confluence) [REIVIN OKTAVIANUS](https://tokopedia.atlassian.net/wiki/people/5dae89dab86cd40c2da5ad2f?ref=confluence) [Rizqi Aryansa](https://tokopedia.atlassian.net/wiki/people/5e25ee87006fae0ca232e1ac?ref=confluence) [Yehezkiel .](https://tokopedia.atlassian.net/wiki/people/5c94aa7a7792242c8613ad14?ref=confluence) [Yusuf Hendrawan](https://tokopedia.atlassian.net/wiki/people/5df336f3f4ab290ecfc64169?ref=confluence)  |
| Product Manager | [Erik Novaldi Sampurno](https://tokopedia.atlassian.net/wiki/people/622e70525b6d710070a01bed?ref=confluence) [Priscilla Christie Suyanto](https://tokopedia.atlassian.net/wiki/people/60865011248ef6007359be97?ref=confluence) [Venessa Jeremia](https://tokopedia.atlassian.net/wiki/people/5ce4e9c983de300fe5d546d7?ref=confluence)  |
| Team | [Minion Stuart](https://tokopedia.atlassian.net/people/team/eeba862a-bd9d-472c-b901-415b15b1a37e?ref=directory&src=peopleMenu) |
| Release date | 16 Oct 2020 / <!--start status:GREY-->MA-3.98<!--end status--> <!--start status:GREY-->SA-2.28<!--end status-->  |
| Module type |  <!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/transaction/sellerorder` |

## Table of Contents

- [Overview](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2205232327/Seller+Order+Management#%5BhardBreak%5DOverview)
- [Release Notes](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2205232327/Seller+Order+Management#Release-Notes)
- [Pages](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2205232327/Seller+Order+Management#Pages)

## Overview

Seller Order Management, usually called SOM, is a module with features that allow sellers to view and manage their physical goods orders. The module includes multiple pages including:

1. Order list page (`SomListFragment`): A page that shows the list of orders. Here seller can view the preview of each order and perform an action depending on the status of the order.
2. Order detail page (`SomDetailFragment`): A page that shows the detail of the order. Here seller can view the detail of an order and perform some action depending on the status of the order.
3. Waiting for payment page (`WaitingPaymentOrderFragment`): A page that shows the list of orders that haven’t been paid by the user. Here seller can only view the preview of the order.
4. Confirm shipping page (`SomConfirmShippingFragment`): A page that allows the seller to confirm that the order will be shipped while also creating a shipment request to the courier provider.
5. Scan AWB page (`SomScanResiActivity`): A page that allows the seller to scan the AWB barcode.
6. Booking code detail page (`SomDetailBookingCodeActivity`): A page that shows the detail of shipment booking detail. Here the seller can view the detail of the shipment.
7. Logistic info detail page (`SomDetailLogisticInfoActivity`): A page that shows some information related to the shipment.
8. Print AWB/label page (`SomPrintAwbActivity`): A page that shows the invoice of the order. Here seller can also print the AWB/label.
9. Order list filter advanced bottom sheet (`SomFilterBottomSheet`): A page that shows the available filters to filter the list of orders that is shown on the order list page. Here the seller has more filter options.
10. Order extension bottom sheet (`SomBottomSheetOrderExtensionRequest`): A page that allows the seller to send a request to the buyer to extend the order processing deadline.
11. Request pick-up page (`SomConfirmReqPickupFragment`): A page that allows the seller to send a requested pick-up to the courier after the seller accepts the order.
12. Edit AWB number bottom sheet (`SomOrderEditAwbBottomSheet`): A bottom sheet that allows the seller to change the AWB number of the order. This bottom sheet can only be opened from an order that has a “Ganti Resi” button.
13. Respond to order cancellation bottom sheet (`SomOrderRequestCancelBottomSheet`): A bottom sheet that allows the seller to respond to the order cancellation request sent by the buyer.

## Release Notes

<!--start expand:27 January 2023 (MA-3.207 & SA-2.137)-->
###### Order extension improvementPR: <https://github.com/tokopedia/android-tokopedia-core/pull/31309>
<!--end expand-->

<!--start expand:5 August 2022 (MA-3.187 & SA-2.117)-->
###### [OM] SkyFallPR: <https://github.com/tokopedia/android-tokopedia-core/pull/27981>
<!--end expand-->

## **Pages**

- [Order list page](/wiki/spaces/PA/pages/2205232373/Order+List)
- [Order detail page](/wiki/spaces/PA/pages/2222952085/Order+Detail)
- Waiting for payment page
- Confirm shipping page
- Scan AWB page
- Booking code detail page
- Logistic info detail page
- Print AWB page
- Order list advanced filter page
- Order extension page
- Request pick-up page
- Edit AWB page
- Respond to buyer cancellation request page
