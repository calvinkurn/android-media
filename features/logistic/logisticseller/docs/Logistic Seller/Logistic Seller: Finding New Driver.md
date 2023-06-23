---
title: "Logistic Seller: Finding New Driver"
labels:
- logistic
- logistic-seller
- finding-new-driver
---

<!--left header table-->
| **Status**              | <!--start status:PURPLE-->DEVELOPMENT<!--end status-->                                                                                                                                                      |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Project Contributor** | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Product Manager         | [Septian Bayu Laksono](https://tokopedia.atlassian.net/wiki/people/5df8541fa0602c0cabdce844?ref=confluence)                                                                                                 |
| Team                    | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                              |
| Release date            | <!--start status:GREY-->NOT YET RELEASED<!--end status-->                                                                                                                                                   |
| Module type             | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                          |
| Product PRD             | [Cari Driver Baru (Finding New Driver) on Seller order management](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2057439075/Cari+Driver+Baru+%28Finding+New+Driver%29+on+Seller+Order+Management)    |
| Module Location         | `features/logistic/logisticseller`                                                                                                                                                                          |

<!--toc-->

## Release Notes

<!--start expand:dd mmm yyyy (MA-x.xxx/SA-x.xxx)-->
###### *not yet released*
<!--end expand-->

## Overview

### Background

Currently, Cari Driver Baru CTA located inside the Lacak Page, since the funnel is to deep most of the sellers didn't aware that Tokopedia has this self service feature. Sellers often requesting to find new driver via Chatbot and this will create the CS Ticket Join Category 'Sulit Dapat Drvier' that Contributes 4.2% of Logistics CS Ticket (as per September 2022). By making the Cari Driver Baru feature prominent on Seller Order List and Order Detail that can be accessed via Three Dots, we aim seller to aware and use this self service feature and bring the CS ticket Join Category 'Sulit Dapat Drvier' down to 2%.

### Project Description

Create a new *Cari Driver Baru* bottomsheet for the seller to finding new driver. This bottomsheet will display the invoice number and action button to find a new driver. There is a countdown timer display if the seller is not yet available to search for drivers and the button will be disabled.

## Navigation

Entry point to show bottomsheet from three dots option on SOM Detail

![](../res/findingnewdriver/Screen%20Shot%202022-12-13%20at%2008.36.39.png)

  
if the request finding new driver is successful it will display a success dialog

![](../res/findingnewdriver/Screen%20Shot%202022-12-13%20at%2008.37.44.png)

but if the request is failed it will display a failed dialog

![](../res/findingnewdriver/Screen%20Shot%202022-12-13%20at%2008.38.15.png)

## How-to

To show find new driver bottomsheet you can call `ApplinkConstInternalLogistic.FIND_NEW_DRIVER` with parameter `order_id` and `invoice`



```
startActivityForResult(RouteManager.getIntent(
  this,
  ApplinkConstInternalLogistic.FIND_NEW_DRIVER
)
  .apply {
      putExtra(LogisticSellerConst.PARAM_ORDER_ID, {order_id})
      putExtra(LogisticSellerConst.PARAM_INVOICE, {invoice})
  }, request_code
)
```

  
There are several resultCode that will be returned in `onActivityResult`



```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
  when (resultCode) {
    Activity.RESULT_OK -> {
      // returned when users success find new driver
    }
    Activity.RESULT_FIRST_USER -> {
      // returned when users failed find new driver
    }
    Activity.RESULT_CANCELED -> {
      // returned when users do nothing / dissmiss the dialog
    }
  }
}
```

### GQL List



| **GQL Name** | **Documentation** | **Description** |
| --- | --- | --- |
| `mpLogisticNewDriverAvailability` | [[GQL] Retry Availability Search New Driver- mpLogisticNewDriverAvailability](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2086543889)  | Used for sellers to get driver availability |
| `MpLogisticNewDriverBooking` | [[GQL] Retry Booking Search New Driver - mpLogisticNewDriverBooking](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/2086805947)  | Used for sellers to search new driver |



---

## Action Items

- Implement new key from SOM Detail to show bottomsheet

## Useful Links

- [Figma](https://www.figma.com/file/LqDp4TeVYb1jiZ51vQPMUk/SOM---Find-New-Driver-v1.1?node-id=17%3A21010&t=E41zdmWUpvFgbWjA-0)
- [Tech Plan](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2089946340/Find+New+Driver)