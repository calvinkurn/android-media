import React, { Component } from 'react'
import Setup_pos from './setup/setup_pos'
import Setup_payment from './setup/setup_payment'
import Setup_cart from './setup/setup_cart'
import Setup_processing from './setup/setup_processing'
import Setup_history from './setup/setup_history'
import DetailCicilan from './setup/setup_cicilan'
import Invoice from './setup/setup_invoice'



class Root extends Component {
  render() {
    const { POS_PAGE } = this.props.data
    
    if (POS_PAGE === 'POS'){
      return <Setup_pos />
    } else if (POS_PAGE === 'PAYMENT'){
      return <Setup_payment data={this.props.data} />
    } else if (POS_PAGE === 'LOCAL_CART'){
      return <Setup_cart />
    } else if (POS_PAGE === 'PROCESSING'){
      return <Setup_processing data={this.props.data} />
    } else if (POS_PAGE === 'HISTORY'){
      return <Setup_history />
    } else if (POS_PAGE === 'CICILAN'){
      return <DetailCicilan />
    } else if (POS_PAGE === 'INVOICE'){
      return <Invoice data={this.props.data} />
    }
  }
}
export default Root