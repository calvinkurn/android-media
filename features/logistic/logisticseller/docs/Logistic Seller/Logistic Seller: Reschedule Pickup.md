---
title: "Logistic Seller: Reschedule Pickup"
labels:
- logistic
- logistic-seller
- reschedule-pickup
---

<!--left header table-->
| **Status**              | <!--start status:GREEN-->RELEASED<!--end status-->                                                                         |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------|
| **Project Contributor** | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence)                      |
| Product Manager         | [Septian Bayu Laksono](https://tokopedia.atlassian.net/wiki/people/5df8541fa0602c0cabdce844?ref=confluence)                |
| Team                    | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                             |
| Release date            | 06 Jul 2022 / <!--start status:GREY-->MA-3.181<!--end status--> <!--start status:GREY-->SA-2.111<!--end status-->          |
| Module type             | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                         |
| Product PRD             | [Reschedule Pickup by Seller](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/1746064426/Reschedule+Pickup+by+Seller) |
| Module Location         | `features/logistic/logisticseller`                                                                                         |

<!--toc-->

## Release Notes

<!--start expand:06 July 2022 (MA-3.181/SA-2.111)-->

<!--end expand-->

## Overview

### Background

Currently, CS Ticket Join Category 'Ubah Jadwal Pickup' Contributes 2.28% of Logistics CS Ticket (as per September 2021), and most of the orders are from GO-JEK and Grab courier (96%). By allowing Seller to have self-service to Reschedule Pickup, we aim to bring the CS ticket down to 1%.

Top 4 reasons from Seller to create CS Ticket  'Ubah Jadwal Pickup' based on [sampling data 23-29 August](https://docs.google.com/spreadsheets/d/1nbk-W71LYWanN4BgQtG7iPJJrpL78yyuB6arikUn_XY/edit#gid=1971677337):

1. Want to reschedule without telling the reason (186) %
2. Want to find a new Driver (133)
3. Shop closed (52)
4. Fake Pickup (29)

### Project Description

Create a new page for reschedule pickup and this page need to select day, time, and reason. After users request reschedule pickup, it will display a successful or failed dialog.

## Navigation

Entry point to show dialog from three dots bottomsheet on SOM Detail when key equals `reschedule_pickup`

![](../res/reschedulepickup/Screen%20Shot%202022-12-09%20at%2013.32.05.png)

  
if the request reschedule pickup is successful it will display a success dialog

![](../res/reschedulepickup/Screen%20Shot%202022-12-09%20at%2016.27.11.png)

  
but if the request reschedule pickup is failed it will display a failed dialog

![](../res/reschedulepickup/Screen%20Shot%202022-12-09%20at%2016.29.06.png)

## How-to

To show reschedule pickup page you can call `ApplinkConstInternalLogistic.RESCHEDULE_PICKUP` with parameter `order_id`



```
startActivityForResult(RouteManager.getIntent(this, ApplinkConstInternalLogistic.RESCHEDULE_PICKUP,  {order_id}))
```

  
There are several `resultCode` that will be returned in `onActivityResult`



```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
  when (resultCode) {
    Activity.RESULT_OK -> {
      // returned when users success reschedule pickup
    }
    else -> {
      // returned when users do nothing / failed reschedule pickup
    }
  }
}
```

### GQL List



| **GQL Name** | **Documentation** | **Description** |
| --- | --- | --- |
| `mpLogisticGetReschedulePickup` | [Get Reschedule Information](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/1848738815/Get+Reschedule+Information)  | Used for sellers get general Information about shipper to be reschedule |
| `mpLogisticInsertReschedulePickup` | [Insert Reschedule Pickup](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/1854605213/Insert+Reschedule+Pickup)  | Used for sellers to insert Reschedule Pickup Action |



---

## Useful Links

- [Figma](https://www.figma.com/file/xXD6gf57nB9mDN8Lu0dj29/%5BUI%2FUX-M%2FD%5D-Re-Schedule-Pickup?node-id=474%3A118693&t=HLGU6hG4bQHx3sRn-0)