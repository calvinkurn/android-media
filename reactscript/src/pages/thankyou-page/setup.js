import React, { Component } from 'react'

import SuccessPage from './SuccessPage'
import TransferPage from './TransferPage'

class Root extends Component {
  render() {
      if (this.props.data.template === 'transfer'){
            return <TransferPage data={this.props.data}/>
       } else if (this.props.data.template === 'instant') {
            return <SuccessPage data={this.props.data}/>
       }
  }
}

export default Root
