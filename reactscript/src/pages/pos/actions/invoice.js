import { NetworkModule } from 'NativeModules'
import { 
  getAddrName
} from '../lib/utility'


export const SEND_EMAIL = 'SEND_EMAIL'
export const sendEmail = (emailAddress, data) => {
  console.log("send email")
  return {
    type: SEND_EMAIL,
    payload: processSendEmail(emailAddress, data)
  }
}

const processSendEmail = async (data) => {
  const outlet_name = await getAddrName()
  const detail_items = getDetailItems(data.items)
  const data_params = {
    email_address: data.email_address,
    bank_name: data.bankName,
    bank_logo_url: data.bankLogo,
    outlet_name,
    transaction_date: data.transaction_date ? data.transaction_date: '',
    final_amount: data.paymentDetails[0].amount,
    invoice_ref_no: data.invoiceRef,
    detail_items
  }
  const sendEmailResponse = await apiSendEmail(`https://o2o-staging.tokopedia.com/o2o/v1/send_email_notification`, data_params)
  return sendEmailResponse
}

const getDetailItems = (items) => {
  return items.map(item => {
      return Object.assign({}, {
        item_name: item.name,
        item_qty: item.quantity,
        item_price: item.price,
        total_price: item.price * item.quantity
      })
  })
}

const apiSendEmail = (url, data) => {
  return NetworkModule.getResponseJson(url, `POST`, JSON.stringify(data), true)
    .then(res => {
      console.log(res)
      return res.data
    })
    .catch(err => {
      console.log(err)
    })
}
