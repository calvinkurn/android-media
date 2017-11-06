import { NetworkModule } from 'NativeModules'
import { 
  getAddrName,
  getEnv,
} from '../lib/utility'
import { 
  BASE_API_URL_PAYMENT,
  BASE_API_URL_SCROOGE,
  BASE_API_URL_PCIDSS, 
  BASE_API_URL,
} from '../lib/api.js'


export const SEND_EMAIL = 'SEND_EMAIL'
export const sendEmailReceipt = (emailAddress, data, items) => {
  return {
    type: SEND_EMAIL,
    payload: processSendEmail(emailAddress, data, items)
  }
}

const processSendEmail = async (emailAddress, data, items) => {
  const outlet_name = await getAddrName()
  const detail_items = getDetailItems(items)
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const data_params = {
    ...data,
    outlet_name,
    detail_items
  }
  const send_email = await proceedSendEmail(data_params, api_url)
  return send_email
}


const proceedSendEmail = async (data_params, api_url) => {
  const url_api = `${api_url.base_api_url}o2o/v2/send_email_notification`
  return NetworkModule.getResponseJson(url_api, `POST`, JSON.stringify(data_params), true)
    .then(res => {
      const resJson = JSON.parse(res)
      if (resJson.status === '200 Ok'){
        return resJson
      }
    })
    .catch(err => {
      console.log(err)
    })
  
}



const getBaseAPI = (env) => {
  let data_api = {}

  if (env === 'production'){
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.PRODUCTION}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.PRODUCTION}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.PRODUCTION}`,
      base_api_url: `${BASE_API_URL.PRODUCTION}`
    }
    return data_api

  } else {
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.STAGING}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.STAGING}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.STAGING}`,
      base_api_url: `${BASE_API_URL.STAGING}`
    }
    return data_api
  }
}


const getDetailItems = (items) => {
  let itemsObj = []
  items.map(item => {
    itemsObj.push({
      item_name: item.Name,
      item_qty: item.Quantity,
      item_price: item.Price,
      total_price: item.Price * item.Quantity
    }) 
  })
  
  return itemsObj
}
